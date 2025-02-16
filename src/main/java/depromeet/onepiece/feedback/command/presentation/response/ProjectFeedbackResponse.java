package depromeet.onepiece.feedback.command.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "프로젝트 피드백 응답 DTO")
public record ProjectFeedbackResponse(
    @Schema(description = "프로젝트 평가 정보", requiredMode = Schema.RequiredMode.REQUIRED)
        ProjectEvaluation projectEvaluation) {

  @Schema(description = "프로젝트 평가 정보")
  public record ProjectEvaluation(
      @Schema(description = "프로젝트 이름", requiredMode = Schema.RequiredMode.REQUIRED)
          String projectName,
      @Schema(
              description = "프로젝트 이미지 링크",
              example = "https://example.com/image.png",
              requiredMode = Schema.RequiredMode.REQUIRED)
          String imageLink,
      @Schema(
              description = "프로젝트 프로세스 단계 완료 여부",
              example = "[true, false, false, true, true]",
              requiredMode = Schema.RequiredMode.REQUIRED)
          List<Boolean> process,
      @Schema(
              description = "프로세스 리뷰",
              example = "프로젝트의 핵심 프로세스 중 일부가 잘 반영되었으며, 개선이 필요한 부분도 존재합니다.",
              requiredMode = Schema.RequiredMode.REQUIRED)
          String processReview,
      @Schema(
              description = "강점 분석",
              example = "# 1. 데이터 기반의 문제 해결\n- A/B 테스트를 통해 효과적인 UX를 도출.",
              requiredMode = Schema.RequiredMode.REQUIRED)
          String strengths,
      @Schema(
              description = "개선 필요 사항",
              example = "# 1. 문장이 너무 길고 가독성이 낮음\n- 내용을 압축하면 더 명확해질 수 있음.",
              requiredMode = Schema.RequiredMode.REQUIRED)
          String areasForImprovement,
      @Schema(description = "페이지별 이슈 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
          List<PageIssue> pageIssues) {}

  @Schema(description = "페이지별 정보")
  public record PageIssue(
      @Schema(description = "페이지 번호", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
          String pageNumber,
      @Schema(
              description = "이슈가 발생한 페이지 이미지 링크",
              example = "https://example.com/page1.png",
              requiredMode = Schema.RequiredMode.REQUIRED)
          String imageLink,
      @Schema(description = "페이지 내 콘텐츠 이슈 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
          List<PageContentIssue> contents) {}

  @Schema(description = "페이지 콘텐츠 이슈 상세 정보")
  public record PageContentIssue(
      @Schema(description = "이슈 유형", example = "문장 오류", requiredMode = Schema.RequiredMode.REQUIRED)
          String type,
      @Schema(
              description = "기존 문장",
              example = "디자인의 다양함을 통해 다재다능함을 보여주는 디자이너입니다.",
              requiredMode = Schema.RequiredMode.REQUIRED)
          String originalSentence,
      @Schema(
              description = "수정된 문장",
              example = "다양한 디자인을 통해 다재다능함을 보여주는 디자이너입니다.",
              requiredMode = Schema.RequiredMode.REQUIRED)
          String revisedSentence) {}
}
