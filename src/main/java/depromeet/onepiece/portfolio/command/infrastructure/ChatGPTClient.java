package depromeet.onepiece.portfolio.command.infrastructure;

import depromeet.onepiece.portfolio.command.presentation.response.ChatGPTRequestDto;
import depromeet.onepiece.portfolio.command.presentation.response.ChatGPTResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@AllArgsConstructor
public class ChatGPTClient {
  private final RestClient restClient;
  private static final String API_URL = ChatGPTConstants.API_URL;
  private final ChatGPTProperties chatGPTProperties;

  public ChatGPTResponseDto sendMessage(ChatGPTRequestDto request) {
    return restClient
        .post()
        .uri(API_URL)
        .header("Authorization", "Bearer " + chatGPTProperties.apiKey())
        .header("Content-Type", "application/json")
        .body(request)
        .retrieve()
        .body(ChatGPTResponseDto.class);
  }
}
