package depromeet.onepiece.portfolio.domain;

import depromeet.onepiece.common.domain.BaseTimeDocument;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio extends BaseTimeDocument {
  @MongoId private ObjectId id;

  @Field(value = "user_id")
  @Indexed
  private ObjectId userId;

  @Field(value = "image_list")
  private List<Image> imageList;

  @Field(value = "pdf_url")
  private String pdfUrl;
}
