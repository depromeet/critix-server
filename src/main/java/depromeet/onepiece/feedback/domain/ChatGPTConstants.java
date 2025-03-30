package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.PROTECTED;

import depromeet.onepiece.common.domain.BaseTimeDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChatGPTConstants extends BaseTimeDocument {

  @MongoId private ObjectId id;

  @Field("overall_prompt")
  private String overallPrompt;

  @Field("overall_schema")
  private String overallSchema;

  @Field("project_prompt")
  private String projectPrompt;

  @Field("project_schema")
  private String projectSchema;
}
