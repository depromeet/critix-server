package depromeet.onepiece.feedback.command.application;

import depromeet.onepiece.feedback.command.infrastructure.ChatGPTService;
import depromeet.onepiece.feedback.command.presentation.request.ChatGPTRequestDto;
import depromeet.onepiece.feedback.command.presentation.response.OverallFeedbackResponseDto;
import depromeet.onepiece.feedback.command.presentation.response.ProjectFeedbackResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackCommandService {
  private final ChatGPTService chatGPTService;

  public OverallFeedbackResponseDto overallFeedback(String portfolioId) {
    // portfolioId 검증
    // portfolioId로 포트폴리오 조회
    // 포트폴리오 이미지 추출
    // chatgptservice의 overallfeedback 포트폴리오 이미지 담아서 호출
    ChatGPTRequestDto requestDto = new ChatGPTRequestDto();
    return chatGPTService.overallFeedback(requestDto);
  }

  public ProjectFeedbackResponseDto projectFeedback(String portfolioId) {
    // portfolioId 검증
    // portfolioId로 포트폴리오 조회
    // 포트폴리오 이미지 추출
    // chatgptservice의 projectfeedback 포트폴리오 이미지 담아서 호출
    ChatGPTRequestDto requestDto = new ChatGPTRequestDto();
    return chatGPTService.projectFeedback(requestDto);
  }

  private ChatGPTRequestDto createOverallFeedbackRequest(String portfolioId) {
    return new ChatGPTRequestDto(
        "gpt-4",
        List.of(
            new ChatGPTRequestDto.Message(
                "user", List.of(new ChatGPTRequestDto.Message.Content("text", "프롬프트", null)))),
        new ChatGPTRequestDto.ResponseFormat("json", null),
        0.7,
        5000,
        1.0,
        0,
        0);
  }

  private ChatGPTRequestDto createProjectFeedbackRequest(String portfolioId) {
    return new ChatGPTRequestDto(
        "gpt-4",
        List.of(
            new ChatGPTRequestDto.Message(
                "user", List.of(new ChatGPTRequestDto.Message.Content("text", "프롬프트", null)))),
        new ChatGPTRequestDto.ResponseFormat("json", null),
        0.8,
        5000,
        1.0,
        0,
        0);
  }
}
