package depromeet.onepiece.feedback.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@AllArgsConstructor
public class EditPair {
  @Field("before_edit")
  private String beforeEdit;

  @Field("after_edit")
  private String afterEdit;
}
