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
public class FeedbackPerPage {

  @Field("page_number")
  private String pageNumber;

  @Field("contents")
  private List<PageFeedbackContent> contents;

  @Field("image_url")
  private String imageUrl;

  public void updateImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
