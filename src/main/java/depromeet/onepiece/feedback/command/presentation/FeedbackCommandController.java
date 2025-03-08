package depromeet.onepiece.feedback.command.presentation;

import depromeet.onepiece.common.error.CustomResponse;
import depromeet.onepiece.feedback.command.infrastructure.FeedbackService;
import depromeet.onepiece.feedback.command.presentation.response.FeedbackResponse;
import depromeet.onepiece.feedback.command.presentation.response.OverallFeedbackResponse;
import depromeet.onepiece.feedback.command.presentation.response.ProjectFeedbackResponse;
import depromeet.onepiece.feedback.command.presentation.response.RecentFeedbackListResponse;
import depromeet.onepiece.feedback.command.presentation.response.RemainCountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
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

  @Operation(summary = "포트폴리오 피드백", description = "포트폴리오 피드백을 반환하는 API [담당자 : 김수진]")
  @PostMapping(value = "")
  public void portfolioFeedback(
      @RequestParam(value = "fileId") String fileId, @RequestBody String additionalChat) {
    feedbackService.portfolioFeedback(fileId, additionalChat);
  }

  @Operation(summary = "포트폴리오 응답 가져오기", description = "포트폴리오 피드백을 feedback id로 가져오기")
  @GetMapping("")
  public ResponseEntity<CustomResponse<FeedbackResponse>> getFeedbackDetail(
      @Parameter(example = "66e516c2b355355088f07c82")
          @RequestParam(defaultValue = "66e516c2b355355088f07c82")
          ObjectId feedbackId) {

    OverallFeedbackResponse overallFeedbackResponse =
        new OverallFeedbackResponse(
            new OverallFeedbackResponse.OverallEvaluation(
                "종합 평가 요약 ",
                new OverallFeedbackResponse.EvaluationDetail(80, "직무 적합성이 높습니다."),
                new OverallFeedbackResponse.EvaluationDetail(75, "논리적인 사고력이 돋보입니다."),
                new OverallFeedbackResponse.EvaluationDetail(85, "글쓰기 표현이 명확합니다."),
                new OverallFeedbackResponse.EvaluationDetail(90, "레이아웃이 가독성이 높습니다.")),
            List.of(
                new OverallFeedbackResponse.FeedbackDetail(
                    "데이터 기반의 디자인 사고", List.of("a/b 테스트 설문조사", "단순한 미적감각이 아니라 뛰어남"))),
            List.of(
                new OverallFeedbackResponse.FeedbackDetail(
                    "UI/UX 디자인 과정을 시각적으로 추가하기", List.of("더 강조할 필요가 있음", "더 개선할 여지가 있음. "))));
    FeedbackResponse feedbackResponse =
        new FeedbackResponse(
            overallFeedbackResponse,
            new ProjectFeedbackResponse(
                new ProjectFeedbackResponse.ProjectEvaluation(
                    "프로젝트 1",
                    "https://avatars.githubusercontent.com/u/108571492?v=4",
                    List.of(true, false, false, false, false),
                    "프로젝트의 핵심 프로세스 중 일부가 잘 반영되었으며, 개선이 필요한 부분도 존재합니다.",
                    "A/B 테스트와 유저 피드백을 활용한 데이터 중심 디자인이 강점입니다.",
                    "페이지 간 일관성이 부족하고, 일부 텍스트의 가독성이 떨어집니다.",
                    List.of(
                        new ProjectFeedbackResponse.PageIssue(
                            "1",
                            "https://avatars.githubusercontent.com/u/108571492?v=4",
                            List.of(
                                new ProjectFeedbackResponse.PageContentIssue(
                                    "번역체/어색한 표현",
                                    "A designer who wants to do many things.",
                                    "다양한 디자인을 통해 다재다능함을 보여주는 디자이너입니다.")))))));
    return CustomResponse.okResponseEntity(feedbackResponse);
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
}
