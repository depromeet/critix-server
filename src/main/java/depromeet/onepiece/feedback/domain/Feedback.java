package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.*;

import depromeet.onepiece.common.domain.BaseTimeDocument;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
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
  private List<ProjectEvaluation> projectEvaluation;
}
