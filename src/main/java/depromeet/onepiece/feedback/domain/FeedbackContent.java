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
public class FeedbackContent {

  @Field("type")
  private FeedbackType type;

  @Field("title")
  private String title;

  @Field("feedback_content_detail_list")
  private List<FeedbackContentDetail> feedbackContentDetailList;
}
