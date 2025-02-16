package depromeet.onepiece.common.config;

import depromeet.onepiece.feedback.command.infrastructure.ChatGPTConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
  private static final String API_URL = ChatGPTConstants.API_URL;

  @Bean
  public RestClient restClient() {
    return RestClient.builder()
        .baseUrl(API_URL)
        .defaultHeader("Content-Type", "application/json")
        .build();
  }
}
