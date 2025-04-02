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
public class OverallEvaluation {

  @Field("summary")
  private String summary;

  @Field("job_fit")
  private EvaluationDetail jobFit;

  @Field("logical_thinking")
  private EvaluationDetail logicalThinking;

  @Field("writing_clarity")
  private EvaluationDetail writingClarity;

  @Field("layout_readability")
  private EvaluationDetail layoutReadability;

  @Field("strengths")
  private List<FeedbackDetail> strengths;

  @Field("improvements")
  private List<FeedbackDetail> improvements;
}
