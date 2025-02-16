package depromeet.onepiece.feedback.command.infrastructure;

import depromeet.onepiece.feedback.command.presentation.request.ChatGPTRequest;
import depromeet.onepiece.feedback.command.presentation.response.ChatGPTResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@AllArgsConstructor
public class ChatGPTClient {
  private final RestClient restClient;
  private static final String API_URL = ChatGPTConstants.API_URL;
  private final ChatGPTProperties chatGPTProperties;

  public ChatGPTResponse sendMessage(ChatGPTRequest request) {
    return restClient
        .post()
        .uri(API_URL)
        .header("Authorization", "Bearer " + chatGPTProperties.apiKey())
        .header("Content-Type", "application/json")
        .body(request)
        .retrieve()
        .body(ChatGPTResponse.class);
  }
}
