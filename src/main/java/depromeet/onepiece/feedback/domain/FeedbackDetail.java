package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.*;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class FeedbackDetail {
  @Field("title")
  private String title;

  @Field("content")
  private List<String> content;
}
