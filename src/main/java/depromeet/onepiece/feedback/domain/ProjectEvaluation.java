package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.*;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ProjectEvaluation {

  @Field("project_name")
  private String projectName;

  @Field("process")
  private List<Boolean> process;

  @Field("process_review")
  private String processReview;

  @Field("positiveFeedback")
  private List<FeedbackDetail> positiveFeedback;

  @Field("negativeFeedback")
  private List<FeedbackDetail> negativeFeedback;

  @Field("feedback_per_page")
  private List<FeedbackPerPage> feedbackPerPage;

  @Field("project_summary")
  private String projectSummary;
}
