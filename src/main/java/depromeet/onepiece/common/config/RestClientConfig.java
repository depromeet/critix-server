package depromeet.onepiece.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient restClient() {
    HttpComponentsClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory();
    requestFactory.setConnectTimeout(10000); // 10 seconds

    RestTemplate restTemplate = new RestTemplate(requestFactory);

    return RestClient.create(restTemplate);
  }
}
