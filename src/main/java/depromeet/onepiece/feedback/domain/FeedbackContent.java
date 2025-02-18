package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.PROTECTED;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = PROTECTED)
class FeedbackContent {

  @Field("type")
  private FeedbackType type;

  @Field("before_edit")
  private String beforeEdit;

  @Field("after_edit")
  private String afterEdit;
}
