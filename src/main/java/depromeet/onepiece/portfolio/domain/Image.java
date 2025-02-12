package depromeet.onepiece.portfolio.domain;

import org.springframework.data.mongodb.core.mapping.Field;

public class Image {

  @Field("file_name")
  private String fileName;

  @Field("text")
  private String text;
}
