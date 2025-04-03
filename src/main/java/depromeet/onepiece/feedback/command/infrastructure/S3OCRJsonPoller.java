package depromeet.onepiece.feedback.command.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import depromeet.onepiece.feedback.command.application.exception.GetOCRResultFailedException;
import depromeet.onepiece.feedback.command.application.exception.OCRResultNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3OCRJsonPoller {

  private final AmazonS3 amazonS3;

  @Value("${cloud.ncp.object-storage.credentials.bucket}")
  private String bucketName;

  @SneakyThrows
  public String waitForResult(String fileId, long timeoutMillis, long intervalMillis) {
    String objectKey = fileId + "/ocr/result.json";

    long startTime = System.currentTimeMillis();

    while (System.currentTimeMillis() - startTime < timeoutMillis) {
      if (amazonS3.doesObjectExist(bucketName, objectKey)) {
        return readFile(objectKey);
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
