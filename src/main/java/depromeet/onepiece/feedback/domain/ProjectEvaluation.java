package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.PROTECTED;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ProjectEvaluation {

  @Field("project_name")
  private String projectName;

  @Field("process")
  private List<Boolean> process;

  @Field("process_review")
  private String processReview;

  @Field("positiveFeedback")
  private String positiveFeedback;

  @Field("negativeFeedback")
  private String negativeFeedback;

  @Field("feedback_per_page")
  private List<FeedbackPerPage> feedbackPerPage;

  @Field("project_summary")
  private String projectSummary;
}
