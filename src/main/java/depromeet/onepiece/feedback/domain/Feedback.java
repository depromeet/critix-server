package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.PROTECTED;

import depromeet.onepiece.common.domain.BaseTimeDocument;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Feedback extends BaseTimeDocument {

  @MongoId private ObjectId id;

  @Field("user_id")
  private ObjectId userId;

  @Field("portfolio_id")
  private ObjectId portfolioId;

  @Field("overall_evaluation")
  private OverallEvaluation overallEvaluation;

  @Field("additional_chat")
  private List<AdditionalChat> additionalChat;

  @Field("project_evaluation")
  private ProjectEvaluation projectEvaluation;
}
