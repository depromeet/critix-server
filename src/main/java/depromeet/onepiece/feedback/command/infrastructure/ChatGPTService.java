package depromeet.onepiece.feedback.command.infrastructure;

import depromeet.onepiece.feedback.command.presentation.request.ChatGPTRequestDto;
import depromeet.onepiece.feedback.command.presentation.response.OverallFeedbackResponseDto;
import depromeet.onepiece.feedback.command.presentation.response.ProjectFeedbackResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGPTService {
  private final ChatGPTClient chatGPTClient;

  public OverallFeedbackResponseDto overallFeedback(ChatGPTRequestDto requestDto) {
    ChatGPTRequestDto updatedRequest =
        new ChatGPTRequestDto(
            requestDto.model(),
            requestDto.messages(),
            requestDto.response_format(),
            0.7,
            5000,
            1.0,
            0,
            0);
    return chatGPTClient.sendMessage(updatedRequest);
  }

  public ProjectFeedbackResponseDto projectFeedback(ChatGPTRequestDto requestDto) {
    ChatGPTRequestDto updatedRequest =
        new ChatGPTRequestDto(
            requestDto.model(),
            requestDto.messages(),
            requestDto.response_format(),
            0.8,
            5000,
            1.0,
            0,
            0);
    return chatGPTClient.sendMessage(updatedRequest);
  }
}
