package depromeet.onepiece.file.command.infrastructure;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.net.URL;
import java.util.*;
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

  public List<String> generatePresignedUrl(String folderPath) {
    List<S3ObjectSummary> objectSummaries =
        amazonS3.listObjects(bucketName, folderPath + "/processed").getObjectSummaries();
    objectSummaries.sort(Comparator.comparing(o -> extractSortableKey(o.getKey())));

    List<String> presignedUrls = new ArrayList<>();
    for (S3ObjectSummary objectSummary : objectSummaries) {
      String objectKey = objectSummary.getKey();
      URL presignedUrl = generatePresignedUrlForObject(objectKey, 60);
      presignedUrls.add(presignedUrl.toString());
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
}
