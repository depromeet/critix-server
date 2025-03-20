package depromeet.onepiece.feedback.command.presentation;

import depromeet.onepiece.common.auth.annotation.CurrentUserId;
import depromeet.onepiece.common.error.CustomResponse;
import depromeet.onepiece.feedback.command.application.FeedbackCommandFacadeService;
import depromeet.onepiece.feedback.command.presentation.response.StartFeedbackResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class FeedbackCommandController {
  private final FeedbackCommandFacadeService feedbackCommandFacadeService;

  @Operation(
      summary = "포트폴리오 피드백 호출",
      description = "fileId 받아 포트폴리오 피드백 API 호출 후 feedbackId 반환하는 API [담당자 : 김수진]")
  @GetMapping(value = "/start")
  public ResponseEntity<CustomResponse<StartFeedbackResponse>> startFeedback(
      @RequestParam(value = "fileId") ObjectId fileId, @CurrentUserId String userId) {
    StartFeedbackResponse response =
        feedbackCommandFacadeService.startFeedback(new ObjectId(userId), fileId);
    return CustomResponse.okResponseEntity(response);
  }
}
