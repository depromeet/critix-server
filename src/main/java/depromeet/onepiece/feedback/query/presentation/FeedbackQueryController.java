package depromeet.onepiece.feedback.query.presentation;

import depromeet.onepiece.common.error.CustomResponse;
import depromeet.onepiece.feedback.command.presentation.response.RecentFeedbackListResponse;
import depromeet.onepiece.feedback.query.application.FeedbackQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Portfolio", description = "포트폴리오 관련 API")
@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackQueryController {
  private final FeedbackQueryService feedbackQueryService;

  @Operation(summary = "전체 피드백 반환", description = "전체 피드백을 반환하는 API [담당자 : 김수진]")
  @GetMapping(value = "/recent/feedback")
  public ResponseEntity<CustomResponse<List<RecentFeedbackListResponse>>> getRecentFeedback(
      @RequestParam(value = "userId") ObjectId userId) {
    List<RecentFeedbackListResponse> feedbackList = feedbackQueryService.getFeedbackList(userId);
    return CustomResponse.okResponseEntity(feedbackList);
  }
}
