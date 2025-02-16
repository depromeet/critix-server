package depromeet.onepiece.feedback.command.infrastructure;

import depromeet.onepiece.feedback.command.presentation.request.ChatGPTRequestDto;
import depromeet.onepiece.feedback.command.presentation.response.ChatGPTResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGPTService {
  private final ChatGPTClient chatGPTClient;

  public ChatGPTResponseDto analyzePortfolio(ChatGPTRequestDto requestDto) {
    return chatGPTClient.sendMessage(requestDto);
  }
}
