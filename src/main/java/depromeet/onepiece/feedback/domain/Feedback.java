package depromeet.onepiece.feedback.domain;

import static depromeet.onepiece.feedback.domain.FeedbackStatus.COMPLETE;
import static depromeet.onepiece.feedback.domain.FeedbackStatus.IN_PROGRESS;
import static depromeet.onepiece.feedback.domain.FeedbackStatus.PENDING;
import static lombok.AccessLevel.*;

import depromeet.onepiece.common.domain.BaseTimeDocument;
import java.util.List;
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
public class Feedback extends BaseTimeDocument {

  @MongoId private ObjectId id;

  @Field("user_id")
  private ObjectId userId;

  @Field("file_id")
  private ObjectId fileId;

  @Field("title")
  private String title;

  @Field("overall_status")
  private FeedbackStatus overallStatus;

  @Field("project_status")
  private FeedbackStatus projectStatus;

  @Field("overall_evaluation")
  private OverallEvaluation overallEvaluation;

  @Field("additional_chat")
  private List<AdditionalChat> additionalChat;

  @Field("project_evaluation")
  private List<ProjectEvaluation> projectEvaluation;

  public static Feedback saveEmptyFeedback(ObjectId userId, ObjectId fileId) {
    return Feedback.builder()
        .userId(userId)
        .fileId(fileId)
        .overallStatus(PENDING)
        .projectStatus(PENDING)
        .build();
  }

  public void updateStatusInProgress() {
    this.overallStatus = IN_PROGRESS;
    this.projectStatus = IN_PROGRESS;
  }

  public void completeEvaluation(
      OverallEvaluation overallEvaluation, List<ProjectEvaluation> projectEvaluation) {
    this.overallStatus = COMPLETE;
    this.projectStatus = COMPLETE;
    this.overallEvaluation = overallEvaluation;
    this.projectEvaluation = projectEvaluation;
  }
}
