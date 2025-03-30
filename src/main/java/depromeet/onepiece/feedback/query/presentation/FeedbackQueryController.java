package depromeet.onepiece.feedback.query.presentation;

import depromeet.onepiece.common.auth.annotation.CurrentUserId;
import depromeet.onepiece.common.error.CustomResponse;
import depromeet.onepiece.feedback.command.presentation.response.RemainCountResponse;
import depromeet.onepiece.feedback.domain.EditPair;
import depromeet.onepiece.feedback.domain.EvaluationDetail;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.FeedbackDetail;
import depromeet.onepiece.feedback.domain.FeedbackPerPage;
import depromeet.onepiece.feedback.domain.FeedbackStatus;
import depromeet.onepiece.feedback.domain.FeedbackType;
import depromeet.onepiece.feedback.domain.OverallEvaluation;
import depromeet.onepiece.feedback.domain.PageFeedbackContent;
import depromeet.onepiece.feedback.domain.ProcessType;
import depromeet.onepiece.feedback.domain.ProjectEvaluation;
import depromeet.onepiece.feedback.query.application.FeedbackQueryFacadeService;
import depromeet.onepiece.feedback.query.presentation.response.RecentFeedbackListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
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

  @Operation(summary = "포트폴리오 응답 가져오기(Mock)", description = "포트폴리오 피드백을 feedback id로 가져오기")
  @GetMapping("")
  public ResponseEntity<CustomResponse<Feedback>> getFeedbackDetail(
      @Parameter(example = "66e516c2b355355088f07c82")
          @RequestParam(defaultValue = "66e516c2b355355088f07c82")
          ObjectId feedbackId) {

    List<FeedbackDetail> strengths =
        List.of(
            new FeedbackDetail("데이터 기반의 디자인 사고", List.of("a/b 테스트 설문조사", "단순한 미적감각이 아니라 뛰어남")),
            new FeedbackDetail(
                "데이터 기반의 디자인 사고",
                List.of("a/b 테스트 설문조사", "단순한 미적감각이 아니라 뛰어남", "a/b 테스트 설문조사", "단순한 미적감각이 아니라 뛰어남")));
    List<FeedbackDetail> improvements =
        List.of(
            new FeedbackDetail(
                "UI/UX 디자인 과정을 시각적으로 추가하기", List.of("더 강조할 필요가 있음", "더 개선할 여지가 있음. ")),
            new FeedbackDetail(
                "UI/UX 디자인 과정을 시각적으로 추가하기", List.of("더 강조할 필요가 있음", "더 개선할 여지가 있음. ")));
    OverallEvaluation overallEvaluation =
        new OverallEvaluation(
            "종합 평가 요약 ",
            new EvaluationDetail(80, "직무 적합성이 높습니다."),
            new EvaluationDetail(75, "논리적인 사고력이 돋보입니다."),
            new EvaluationDetail(85, "글쓰기 표현이 명확합니다."),
            new EvaluationDetail(90, "레이아웃이 가독성이 높습니다."),
            strengths,
            improvements);

    ProjectEvaluation projectEvaluation =
        new ProjectEvaluation(
            "프로젝트 1",
            "25.png",
            List.of(
                ProcessType.SOSO,
                ProcessType.GOOD,
                ProcessType.BAD,
                ProcessType.GOOD,
                ProcessType.GOOD),
            "회고가 빠져있습니다",
            List.of(
                new FeedbackDetail(
                    "데이터 기반의 문제 해결", List.of("단순한 ui 개선이아닌 ", "새소식 사용자의 50%가 모른다 답변")),
                new FeedbackDetail(
                    "데이터 기반의 문제 해결", List.of("단순한 ui 개선이아닌 ", "새소식 사용자의 50%가 모른다 답변"))),
            List.of(
                new FeedbackDetail("문장이 길고 가독성이 낮음", List.of("설명이 너무 상세하여 핵심을 빠르게 파악하기 어려움")),
                new FeedbackDetail("문장이 길고 가독성이 낮음", List.of("설명이 너무 상세하여 핵심을 빠르게 파악하기 어려움"))),
            List.of(
                new FeedbackPerPage(
                    "1.png",
                    List.of(
                        new PageFeedbackContent(
                            FeedbackType.LOGICAL_LEAP,
                            "논리적 비약",
                            List.of(
                                new EditPair("푸시 의존도가 높은 점이 문제...", "푸시 의존도가 높은 것이 문제임..."),
                                new EditPair("새소식 문제점 파악을 위해...", "새소식의 문제점을 파악하기 위해..."))))),
                new FeedbackPerPage(
                    "6.png",
                    List.of(
                        new PageFeedbackContent(
                            FeedbackType.LENGTH_OR_READABILITY,
                            "문장이 길거나 가독성이 떨어지는 표현 수정",
                            List.of(new EditPair("새소식 문제점 파악을 위해...", "새소식의 문제점을 파악하기 위해...")))))),
            "데이터 기반 UX 개선과 A/B 테스트를 활용한 성장 전략이 돋보이는 프로젝트! 하지만 CTR 증가의 사업적 의미와 유저 락인 효과를 더욱 명확히 설명하면 설득력이 더 좋아질 것 같아요");

    Feedback feedback =
        new Feedback(
            new ObjectId(),
            new ObjectId(),
            new ObjectId(),
            "장다혜_포트폴리오_250104",
            FeedbackStatus.PENDING,
            FeedbackStatus.PENDING,
            overallEvaluation,
            new ArrayList<>(),
            List.of(projectEvaluation));
    return CustomResponse.okResponseEntity(feedback);
  }

  @Operation(summary = "남은 피드백 횟수 조회", description = "남은 피드백 횟수 조회")
  @GetMapping("/remain")
  public ResponseEntity<CustomResponse<RemainCountResponse>> getRemainCount(
      @CurrentUserId ObjectId userId) {
    Long remainCount = feedbackQueryFacadeService.getRemainCount(userId);
    RemainCountResponse remainCountResponse = new RemainCountResponse(remainCount.intValue());
    return CustomResponse.okResponseEntity(remainCountResponse);
  }
}
