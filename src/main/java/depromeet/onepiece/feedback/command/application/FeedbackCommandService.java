package depromeet.onepiece.feedback.command.application;

import depromeet.onepiece.feedback.command.infrastructure.ChatGPTService;
import depromeet.onepiece.feedback.command.presentation.request.ChatGPTRequest;
import depromeet.onepiece.feedback.command.presentation.response.OverallFeedbackResponse;
import depromeet.onepiece.feedback.command.presentation.response.ProjectFeedbackResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackCommandService {
  private final ChatGPTService chatGPTService;

  public OverallFeedbackResponse overallFeedback(String portfolioId) {
    // portfolioId 검증
    // portfolioId로 포트폴리오 조회
    // 포트폴리오 이미지 추출
    // chatgptservice의 overallfeedback 포트폴리오 이미지 담아서 호출
    ChatGPTRequest requestDto = createOverallFeedbackRequest(portfolioId);
    return chatGPTService.overallFeedback(requestDto);
  }

  public ProjectFeedbackResponse projectFeedback(String portfolioId) {
    // portfolioId 검증
    // portfolioId로 포트폴리오 조회
    // 포트폴리오 이미지 추출
    // chatgptservice의 projectfeedback 포트폴리오 이미지 담아서 호출
    ChatGPTRequest requestDto = createProjectFeedbackRequest(portfolioId);
    return chatGPTService.projectFeedback(requestDto);
  }

  private ChatGPTRequest createOverallFeedbackRequest(String portfolioId) {
    return new ChatGPTRequest(
        "gpt-4",
        List.of(
            new ChatGPTRequest.Message(
                "user", List.of(new ChatGPTRequest.Message.Content("text", "프롬프트", null)))),
        new ChatGPTRequest.ResponseFormat("json", null),
        0.7,
        5000,
        1.0,
        0,
        0);
  }

  private ChatGPTRequest createProjectFeedbackRequest(String portfolioId) {
    return new ChatGPTRequest(
        "gpt-4",
        List.of(
            new ChatGPTRequest.Message(
                "user", List.of(new ChatGPTRequest.Message.Content("text", "프롬프트", null)))),
        new ChatGPTRequest.ResponseFormat("json", null),
        0.8,
        5000,
        1.0,
        0,
        0);
  }
}
