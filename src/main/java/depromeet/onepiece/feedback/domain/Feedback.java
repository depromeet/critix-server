package depromeet.onepiece.feedback.domain;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback {

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
