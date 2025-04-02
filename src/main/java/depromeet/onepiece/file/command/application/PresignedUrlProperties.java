package depromeet.onepiece.file.command.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.ncp.object-storage")
public record PresignedUrlProperties(String endpoint, Credentials credentials) {
  public String bucket() {
    return credentials.bucket();
  }

  public record Credentials(String bucket) {}
}
