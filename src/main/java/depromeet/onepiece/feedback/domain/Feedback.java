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
    if (overallStatus != COMPLETE) this.overallStatus = IN_PROGRESS;
    if (projectStatus != COMPLETE) this.projectStatus = IN_PROGRESS;
  }

  public void completeOverallEvaluation(OverallEvaluation overallEvaluation) {
    this.overallStatus = COMPLETE;
    this.overallEvaluation = overallEvaluation;
  }

  public void completeProjectEvaluation(List<ProjectEvaluation> projectEvaluation) {
    this.projectStatus = COMPLETE;
    this.projectEvaluation = projectEvaluation;
  }

  public void updateTitle(String title) {
    this.title = title;
  }
}
