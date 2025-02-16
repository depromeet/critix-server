package depromeet.onepiece.feedback.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvaluationItem {

  @Field("evaluation_type")
  private depromeet.onepiece.feedback.domain.EvaluationType evaluationType;

  @Field("evaluation_content")
  private String evaluationContent;

  @Field("evaluation_score")
  private int evaluationScore;
}
