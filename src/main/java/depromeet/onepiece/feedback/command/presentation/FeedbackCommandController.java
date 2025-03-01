package depromeet.onepiece.feedback.command.presentation;

import com.azure.core.annotation.QueryParam;
import depromeet.onepiece.feedback.command.infrastructure.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Portfolio", description = "포트폴리오 관련 API")
@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackCommandController {
  private final FeedbackService feedbackService;

  @Operation(summary = "포트폴리오 피드백", description = "포트폴리오 피드백을 반환하는 API [담당자 : 김수진]")
  @PostMapping(value = "")
  public void portfolioFeedback(
      @QueryParam(value = "fileId") String fileId, @RequestBody String additionalChat) {
    feedbackService.portfolioFeedback(fileId, additionalChat);
  }
}
