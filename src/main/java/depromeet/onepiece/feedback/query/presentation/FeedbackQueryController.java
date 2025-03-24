package depromeet.onepiece.feedback.query.presentation;

import depromeet.onepiece.common.auth.annotation.CurrentUserId;
import depromeet.onepiece.common.error.CustomResponse;
import depromeet.onepiece.feedback.command.presentation.response.RemainCountResponse;
import depromeet.onepiece.feedback.query.application.FeedbackQueryFacadeService;
import depromeet.onepiece.feedback.query.presentation.response.FeedbackDetailResponse;
import depromeet.onepiece.feedback.query.presentation.response.RecentFeedbackListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
  private final FeedbackQueryFacadeService feedbackQueryFacadeService;

  @Operation(summary = "전체 피드백 반환", description = "전체 피드백을 반환하는 API [담당자 : 김수진]")
  @GetMapping(value = "/recent/feedback")
  public ResponseEntity<CustomResponse<List<RecentFeedbackListResponse>>> getRecentFeedback(
      @RequestParam(value = "userId") ObjectId userId) {
    List<RecentFeedbackListResponse> feedbackList =
        feedbackQueryFacadeService.getFeedbackList(userId);
    return CustomResponse.okResponseEntity(feedbackList);
  }

  @Operation(summary = "포트폴리오 응답 가져오기", description = "포트폴리오 피드백을 feedback id로 가져오기")
  @GetMapping("")
  public ResponseEntity<CustomResponse<FeedbackDetailResponse>> getFeedbackDetail(
      @Parameter(example = "66e516c2b355355088f07c82") @RequestParam ObjectId feedbackId) {
    return CustomResponse.okResponseEntity(feedbackQueryFacadeService.getFeedback(feedbackId));
  }

  @Operation(summary = "최근 포폴 피드백 목록", description = "최근 피드백 목록 기본 날짜정렬 ")
  @GetMapping("/recent")
  public ResponseEntity<CustomResponse<List<RecentFeedbackListResponse>>> recentFeedbackList(
      @CurrentUserId ObjectId userId) {
    return CustomResponse.okResponseEntity(feedbackQueryFacadeService.getRecentFeedback(userId));
  }

  @Operation(summary = "남은 피드백 횟수 조회(Mock)", description = "남은 피드백 횟수 조회")
  @GetMapping("/remain")
  public ResponseEntity<CustomResponse<RemainCountResponse>> getRemainCount() {
    return CustomResponse.okResponseEntity(new RemainCountResponse(5));
  }
}
