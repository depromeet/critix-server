package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.PROTECTED;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = PROTECTED)
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
  private FeedbackDetail strengths;

  @Field("improvements")
  private FeedbackDetail improvements;
}
