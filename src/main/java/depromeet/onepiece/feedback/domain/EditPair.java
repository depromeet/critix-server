package depromeet.onepiece.feedback.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EditPair {
  @Field("before_edit")
  private String beforeEdit;

  @Field("after_edit")
  private String afterEdit;
}
