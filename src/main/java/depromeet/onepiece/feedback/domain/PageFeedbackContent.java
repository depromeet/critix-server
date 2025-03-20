package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PageFeedbackContent {

  @Field("type")
  private FeedbackType type;

  @Field("title")
  private String title;

  @Field("after_edit")
  private String afterEdit;

  @Field("before_edit")
  private String beforeEdit;
}
