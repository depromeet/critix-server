package depromeet.onepiece.feedback.command.presentation.response;

import java.util.List;

public record ProjectFeedbackResponseDto(ProjectEvaluation projectEvaluation, String timestamp) {
  public record ProjectEvaluation(
      String projectName,
      String imageLink,
      List<Boolean> process,
      String processReview,
      String strengths,
      String areasForImprovement,
      List<PageIssue> pageIssues) {}

  public record PageIssue(String pageNumber, String imageLink, List<PageContentIssue> contents) {}

  public record PageContentIssue(String type, String originalSentence, String revisedSentence) {}
}
