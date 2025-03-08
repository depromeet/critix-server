package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.*;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/** 확장을 고려해서 일단 document로 저장 */
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class FeedbackPerPage {

  @Field("page_number")
  private String pageNumber;

  @Field("contents")
  private List<FeedbackContent> contents;

  @Field("image_url")
  private String imageUrl;
}
