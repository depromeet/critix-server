package depromeet.onepiece.common.eventsourcing.dto;

import depromeet.onepiece.feedback.domain.FeedbackStatus;
import java.util.UUID;
import lombok.Builder;
import org.bson.types.ObjectId;

@Builder
public record GPTFeedbackStatusTopic(
    String id,
    String userId,
    String fileId,
    String feedbackId,
    FeedbackStatus feedbackStatus,
    FeedbackStatus projectStatus,
    int retryCount) {
  public static GPTFeedbackStatusTopic of(
      ObjectId userId,
      ObjectId fileId,
      ObjectId feedbackId,
      FeedbackStatus feedbackStatus,
      FeedbackStatus projectStatus,
      int retryCount) {
    return GPTFeedbackStatusTopic.builder()
        .id(UUID.randomUUID().toString())
        .userId(userId.toString())
        .fileId(fileId.toString())
        .feedbackId(feedbackId.toString())
        .feedbackStatus(feedbackStatus)
        .projectStatus(projectStatus)
        .retryCount(retryCount)
        .build();
  }

  public static GPTFeedbackStatusTopic increaseRetryCount(GPTFeedbackStatusTopic topic) {
    return GPTFeedbackStatusTopic.builder()
        .id(topic.id())
        .userId(topic.userId())
        .fileId(topic.fileId())
        .feedbackId(topic.feedbackId())
        .feedbackStatus(topic.feedbackStatus())
        .projectStatus(topic.projectStatus())
        .retryCount(topic.retryCount() + 1)
        .build();
  }
}
