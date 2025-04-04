package depromeet.onepiece.file.command.application;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PresignedUrlGenerator {

  private final AmazonS3 amazonS3;

  @Value("${cloud.ncp.object-storage.credentials.bucket}")
  private String bucketName;

  private final String s3Host = "https://kr.object.ncloudstorage.com/";

  public List<String> generatePresignedUrl(String folderPath) {
    List<S3ObjectSummary> objectSummaries =
        amazonS3.listObjects(bucketName, folderPath + "/processed").getObjectSummaries();

    objectSummaries.sort(Comparator.comparing(o -> extractSortableKey(o.getKey())));

    List<String> presignedUrls = new ArrayList<>();

    for (S3ObjectSummary objectSummary : objectSummaries) {
      String objectKey = objectSummary.getKey();
      if (!objectKey.endsWith("png")) {
        continue;
      }
      URL presignedUrl = generatePresignedUrlForObject(objectKey, 30);
      // url로 보내면 image 다운로드 에러떠서 base64로 변경
      // presignedUrls.add(convertBase64Url(presignedUrl.toString()));
    }

    return presignedUrls;
  }

  private URL generatePresignedUrlForObject(String objectKey, int expirationMinutes) {
    Date expiration = new Date();
    expiration.setTime(System.currentTimeMillis() + expirationMinutes * 60 * 1000);

    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucketName, objectKey)
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration);

    return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
  }

  private String extractSortableKey(String filename) {
    Pattern pattern = Pattern.compile("(\\d+)");
    Matcher matcher = pattern.matcher(filename);
    StringBuffer stringBuffer = new StringBuffer();

    while (matcher.find()) {
      String number = matcher.group(1);
      matcher.appendReplacement(stringBuffer, String.format("%020d", Long.parseLong(number)));
    }
    matcher.appendTail(stringBuffer);
    return stringBuffer.toString();
  }

  private String convertBase64Url(String s3Url) {
    try {
      URL url = new URL(s3Url);
      try (InputStream in = url.openStream()) {
        byte[] imageBytes = in.readAllBytes(); // Java 9+
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String generatePresignedUrlForKey(String objectKey) {
    try {
      URL presignedUrl = generatePresignedUrlForObject(objectKey, 600);
      return presignedUrl.toString();
    } catch (Exception e) {
      throw new RuntimeException("Presigned URL 생성 실패: " + objectKey, e);
    }
  }
}
