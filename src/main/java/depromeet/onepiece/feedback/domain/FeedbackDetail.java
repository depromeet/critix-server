package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.PROTECTED;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedbackDetail {
  @Field("title")
  private String title;

  @Field("contents")
  private String content;
}
