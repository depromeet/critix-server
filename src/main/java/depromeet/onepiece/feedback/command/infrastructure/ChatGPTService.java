package depromeet.onepiece.feedback.command.infrastructure;

import depromeet.onepiece.feedback.command.presentation.request.ChatGPTRequest;
import depromeet.onepiece.feedback.command.presentation.response.OverallFeedbackResponse;
import depromeet.onepiece.feedback.command.presentation.response.ProjectFeedbackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGPTService {
  private final ChatGPTClient chatGPTClient;

  public OverallFeedbackResponse overallFeedback(ChatGPTRequest requestDto) {
    ChatGPTRequest updatedRequest =
        new ChatGPTRequest(
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

  public ProjectFeedbackResponse projectFeedback(ChatGPTRequest requestDto) {
    ChatGPTRequest updatedRequest =
        new ChatGPTRequest(
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
