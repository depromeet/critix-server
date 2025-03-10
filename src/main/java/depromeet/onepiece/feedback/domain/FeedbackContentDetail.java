package depromeet.onepiece.feedback.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FeedbackContentDetail {
  @Field("after_edit")
  private String afterEdit;

  @Field("before_edit")
  private String beforeEdit;
}
