package depromeet.onepiece.feedback.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class EvaluationDetail {

  @Field("score")
  private int score;

  @Field("review")
  private String review;
}
