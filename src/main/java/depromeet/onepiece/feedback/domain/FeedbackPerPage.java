package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.PROTECTED;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/** 확장을 고려해서 일단 document로 저장 */
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedbackPerPage {

  @Field("page_number")
  private String pageNumber;

  @Field("contents")
  private List<FeedbackContent> contents;
}
