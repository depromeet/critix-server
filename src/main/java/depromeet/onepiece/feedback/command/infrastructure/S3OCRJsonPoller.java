package depromeet.onepiece.feedback.command.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import depromeet.onepiece.common.utils.ConvertService;
import depromeet.onepiece.feedback.command.application.exception.GetOCRResultFailedException;
import depromeet.onepiece.feedback.command.application.exception.OCRResultNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3OCRJsonPoller {

  private final AmazonS3 amazonS3;
  private final ObjectMapper objectMapper;

  @Value("${cloud.ncp.object-storage.credentials.bucket}")
  private String bucketName;

  @SneakyThrows
  public String waitForResult(String fileId, long timeoutMillis, long intervalMillis) {
    String objectKey = fileId + "/ocr/result.json";

    long startTime = System.currentTimeMillis();

    while (System.currentTimeMillis() - startTime < timeoutMillis) {
      if (amazonS3.doesObjectExist(bucketName, objectKey)) {
        String ocrResult = readFile(objectKey);
        JsonNode jsonNode = ConvertService.readTree(ocrResult);
        ObjectNode original = (ObjectNode) jsonNode;
        ObjectNode sorted = objectMapper.createObjectNode();

        List<String> fieldNames = new ArrayList<>();
        original.fieldNames().forEachRemaining(fieldNames::add);

        // 숫자 부분 기준으로 정렬
        fieldNames.sort(
            Comparator.comparingInt(
                name -> {
                  try {
                    return Integer.parseInt(name.replaceAll("\\D", "")); // 숫자만 추출
                  } catch (NumberFormatException e) {
                    return Integer.MAX_VALUE; // 숫자가 없으면 뒤로 보냄
                  }
                }));

        for (String fieldName : fieldNames) {
          sorted.set(fieldName, original.get(fieldName));
        }
        return sorted.toPrettyString();
      }
      Thread.sleep(intervalMillis);
    }

    throw new OCRResultNotFoundException();
  }

  private String readFile(String objectKey) {
    try (S3Object s3Object = amazonS3.getObject(bucketName, objectKey);
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(s3Object.getObjectContent()))) {
      return reader.lines().collect(Collectors.joining("\n"));
    } catch (Exception e) {
      throw new GetOCRResultFailedException();
    }
  }
}
