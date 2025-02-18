package depromeet.onepiece.feedback.domain;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectEvaluation {

  @Field("project_name")
  private String projectName;

  @Field("process")
  private List<Boolean> process;

  @Field("process_review")
  private String processReview;

  @Field("strengths")
  private String strengths;

  @Field("areas_for_improvement")
  private String areasForImprovement;

  @Field("feedback_per_page")
  private List<FeedbackPerPage> feedbackPerPage;

  @Field("project_summary")
  private String projectSummary;
}
