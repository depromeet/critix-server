package depromeet.onepiece.common.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUploadConfig {

  @Value("${cloud.ncp.object-storage.credentials.access-key}")
  private String accessKey;

  @Value("${cloud.ncp.object-storage.credentials.secret-key}")
  private String secretKey;

  @Value("${cloud.ncp.object-storage.endpoint}")
  private String endpoint;

  @Value("${cloud.ncp.region.static-region}")
  private String region;

  @Bean
  public AmazonS3 amazonS3() {
    BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

    return AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .build();
  }
}
