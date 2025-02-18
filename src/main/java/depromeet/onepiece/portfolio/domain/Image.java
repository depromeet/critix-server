package depromeet.onepiece.portfolio.domain;

import static lombok.AccessLevel.PROTECTED;

import depromeet.onepiece.common.domain.BaseTimeDocument;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class Image extends BaseTimeDocument {

  @Field("file_name")
  private String fileName;

  @Field("text")
  private String text;
}
