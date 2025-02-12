package depromeet.onepiece.feedback.domain;

import org.springframework.data.mongodb.core.mapping.Field;

public class EvaluationItem {

  @Field("evaluation_type")
  private EvaluationType evaluationType;

  @Field("evaluation_content")
  private String evaluationContent;

  @Field("evaluation_score")
  private int evaluationScore;
}
