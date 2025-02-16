package depromeet.onepiece.feedback.command.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.List;

public record OverallFeedbackResponse(
    @Schema(description = "전체 평가", requiredMode = RequiredMode.REQUIRED)
        OverallEvaluation overallEvaluation,
    @Schema(description = "강점 분석", requiredMode = RequiredMode.REQUIRED)
        List<FeedbackDetail> strengths,
    @Schema(description = "개선할 점 및 해결방안", requiredMode = RequiredMode.REQUIRED)
        List<FeedbackDetail> improvements) {

  public record OverallEvaluation(
      @Schema(description = "종합 평가 요약", requiredMode = RequiredMode.REQUIRED) String summary,
      @Schema(description = "직무 적합성", requiredMode = RequiredMode.REQUIRED) EvaluationDetail jobFit,
      @Schema(description = "논리적 사고 평가", requiredMode = RequiredMode.REQUIRED)
          EvaluationDetail logicalThinking,
      @Schema(description = "문장 가독성 평가", requiredMode = RequiredMode.REQUIRED)
          EvaluationDetail writingClarity,
      @Schema(description = "레이아웃 가독성 평가", requiredMode = RequiredMode.REQUIRED)
          EvaluationDetail layoutReadability) {}

  public record EvaluationDetail(
      @Schema(description = "점수 (0-100 범위)", requiredMode = RequiredMode.REQUIRED) int score,
      @Schema(description = "평가", requiredMode = RequiredMode.REQUIRED) String review) {}

  public record FeedbackDetail(
      @Schema(description = "제목", example = "스토리텔링이 강한 포트폴리오", requiredMode = RequiredMode.REQUIRED)
          String title,
      @Schema(
              description = "내용",
              example = "단순한 디자인을 넘어서는 창의적인 아이디어가 돋보입니다.",
              requiredMode = RequiredMode.REQUIRED)
          String content) {}
}
