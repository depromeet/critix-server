package depromeet.onepiece.feedback.command.presentation.response;

import java.util.List;

public record OverallFeedbackResponseDto(
    OverallEvaluation overallEvaluation,
    List<FeedbackDetail> strengths,
    List<FeedbackDetail> improvements,
    String timestamp) {
  public record OverallEvaluation(
      String summary,
      EvaluationDetail jobFit,
      EvaluationDetail logicalThinking,
      EvaluationDetail writingClarity,
      EvaluationDetail layoutReadability) {}

  public record EvaluationDetail(int score, String review) {}

  public record FeedbackDetail(String title, String content) {}
}
