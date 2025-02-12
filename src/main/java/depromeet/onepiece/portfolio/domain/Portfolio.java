package depromeet.onepiece.portfolio.domain;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
public class Portfolio {
  @MongoId private ObjectId id;

  @Field(value = "user_id")
  private ObjectId userId;

  @Field(value = "image_list")
  private List<Image> imageList;

  @Field(value = "pdf_url")
  private String pdfUrl;
}
