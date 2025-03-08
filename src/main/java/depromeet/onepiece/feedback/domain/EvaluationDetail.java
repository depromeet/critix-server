package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class EvaluationDetail {

  @Field("score")
  private int score;

  @Field("review")
  private String review;
}
