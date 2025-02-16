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
  private depromeet.onepiece.feedback.domain.OverallEvaluation overallEvaluation;

  @Field("feedback_per_page")
  private List<depromeet.onepiece.feedback.domain.FeedbackPerPage> feedbackPerPage;

  @Field("additional_chat")
  private List<depromeet.onepiece.feedback.domain.AdditionalChat> additionalChat;

  @Field("feedback_per_project")
  private List<depromeet.onepiece.feedback.domain.FeedbackPerProject> feedbackPerProjects;
}
