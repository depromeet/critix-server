package depromeet.onepiece.feedback.command.presentation;

import depromeet.onepiece.common.error.CustomResponse;
import depromeet.onepiece.feedback.command.infrastructure.AzureService;
import depromeet.onepiece.feedback.command.infrastructure.FeedbackService;
import depromeet.onepiece.feedback.command.presentation.response.RecentFeedbackListResponse;
import depromeet.onepiece.feedback.command.presentation.response.RemainCountResponse;
import depromeet.onepiece.feedback.domain.EvaluationDetail;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.FeedbackContent;
import depromeet.onepiece.feedback.domain.FeedbackDetail;
import depromeet.onepiece.feedback.domain.FeedbackPerPage;
import depromeet.onepiece.feedback.domain.FeedbackType;
import depromeet.onepiece.feedback.domain.OverallEvaluation;
import depromeet.onepiece.feedback.domain.ProjectEvaluation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Portfolio", description = "포트폴리오 관련 API")
@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackCommandController {
  private final FeedbackService feedbackService;
  private final AzureService azureService;

  //  @Operation(summary = "포트폴리오 피드백", description = "포트폴리오 피드백을 반환하는 API [담당자 : 김수진]")
  //  @PostMapping(value = "")
  //  public void portfolioFeedback(
  //      @RequestParam(value = "fileId") String fileId, @RequestBody String additionalChat) {
  //    feedbackService.portfolioFeedback(fileId, additionalChat);
  //  }

  @Operation(summary = "포트폴리오 응답 가져오기", description = "포트폴리오 피드백을 feedback id로 가져오기")
  @GetMapping("")
  public ResponseEntity<CustomResponse<Feedback>> getFeedbackDetail(
      @Parameter(example = "66e516c2b355355088f07c82")
          @RequestParam(defaultValue = "66e516c2b355355088f07c82")
          ObjectId feedbackId) {

    List<FeedbackDetail> strengths =
        List.of(new FeedbackDetail("데이터 기반의 디자인 사고", List.of("a/b 테스트 설문조사", "단순한 미적감각이 아니라 뛰어남")));
    List<FeedbackDetail> improvements =
        List.of(
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
            List.of(true, false, false, false, false),
            "회고가 빠져있습니다",
            List.of(
                new FeedbackDetail(
                    "데이터 기반의 문제 해결", List.of("단순한 ui 개선이아닌 ", "새소식 사용자의 50%가 모른다 답변"))),
            List.of(new FeedbackDetail("문장이 길고 가독성이 낮음", List.of("설명이 너무 상세하여 핵심을 빠르게 파악하기 어려움"))),
            List.of(
                new FeedbackPerPage(
                    "1",
                    List.of(new FeedbackContent(FeedbackType.LOGICAL_LEAP, "기존문장", "바뀐문장")),
                    "https://avatars.githubusercontent.com/u/108571492?v=4")),
            "전체적으로 훌륭한 프로젝트입니다.");
    Feedback feedback =
        new Feedback(
            new ObjectId(),
            new ObjectId(),
            new ObjectId(),
            overallEvaluation,
            new ArrayList<>(),
            projectEvaluation);
    return CustomResponse.okResponseEntity(feedback);
  }

  @Operation(summary = "최근 포폴 피드백 목록", description = "최근 피드백 목록 기본 날짜정렬 ")
  @GetMapping("/recent")
  public ResponseEntity<CustomResponse<List<RecentFeedbackListResponse>>> recentFeedbackList() {
    return CustomResponse.okResponseEntity(
        List.of(
            new RecentFeedbackListResponse(
                new ObjectId("66e516c2b355355088f07c82"), LocalDateTime.now(), "포폴 이름"),
            new RecentFeedbackListResponse(
                new ObjectId("66e516c2b355355088f07c82"), LocalDateTime.now(), "포폴 이름2"),
            new RecentFeedbackListResponse(
                new ObjectId("66e516c2b355355088f07c82"), LocalDateTime.now(), "포폴 이름3")));
  }

  @Operation(summary = "남은 피드백 횟수 조회", description = "남은 피드백 횟수 조회")
  @GetMapping("/remain")
  public ResponseEntity<CustomResponse<RemainCountResponse>> getRemainCount() {
    return CustomResponse.okResponseEntity(new RemainCountResponse(5));
  }

  @Operation(summary = "포트폴리오 피드백", description = "포트폴리오 피드백을 반환하는 API [담당자 : 김수진]")
  @PostMapping(value = "")
  public ResponseEntity<CustomResponse<String>> getTest(
      @RequestParam(value = "fileId") String fileId, @RequestBody String additionalChat) {
    String feedback = feedbackService.portfolioFeedback(fileId, "");
    return CustomResponse.okResponseEntity(feedback);
  }
}
