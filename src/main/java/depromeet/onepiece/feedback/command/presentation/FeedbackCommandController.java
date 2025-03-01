package depromeet.onepiece.feedback.command.presentation;

import com.azure.core.annotation.QueryParam;
import depromeet.onepiece.feedback.command.infrastructure.AzureTest;
import depromeet.onepiece.feedback.command.infrastructure.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Portfolio", description = "포트폴리오 관련 API")
@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackCommandController {
  private final FeedbackService feedbackService;
  private final AzureTest azureTest;

  @GetMapping("/run")
  public Map<String, Object> runAssistant() {
    return azureTest.runAssistant();
  }

  @Operation(summary = "포트폴리오 피드백", description = "포트폴리오 피드백을 반환하는 API [담당자 : 김수진]")
  @GetMapping(value = "")
  public void portfolioFeedback(@QueryParam(value = "fileId") String portfolioId) {
    feedbackService.portfolioFeedback(portfolioId);
  }
}
