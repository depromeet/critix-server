package depromeet.onepiece.feedback.command.presentation;

import depromeet.onepiece.feedback.command.application.FeedbackCommandService;
import depromeet.onepiece.feedback.command.presentation.response.OverallFeedbackResponseDto;
import depromeet.onepiece.feedback.command.presentation.response.ProjectFeedbackResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Portfolio", description = "포트폴리오 관련 API")
@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
public class FeedbackCommandController {
  private final FeedbackCommandService portfolioCommandService;

  @Operation(summary = "포트폴리오 종합평가 반환", description = "종합평가를 반환하는 API")
  @GetMapping("/")
  public OverallFeedbackResponseDto overallFeedback(@RequestParam String portfolioId) {
    return portfolioCommandService.overallFeedback(portfolioId);
  }

  @Operation(summary = "포트폴리오 프로젝트 별 피드백 반환", description = "프로젝트 별 피드백을 반환하는 API")
  @GetMapping("/")
  public ProjectFeedbackResponseDto projectFeedback(@RequestParam String portfolioId) {
    return portfolioCommandService.projectFeedback(portfolioId);
  }
}
