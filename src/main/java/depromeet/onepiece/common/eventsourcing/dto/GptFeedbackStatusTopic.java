package depromeet.onepiece.common.eventsourcing.dto;

import depromeet.onepiece.feedback.domain.FeedbackStatus;
import java.util.UUID;
import lombok.Builder;
import org.bson.types.ObjectId;

@Builder
public record GptFeedbackStatusTopic(
    String id,
    ObjectId userId,
    ObjectId portfolioFileId,
    FeedbackStatus feedbackStatus,
    FeedbackStatus projectStatus,
    int retryCount) {
  public static GptFeedbackStatusTopic of(
      ObjectId userId,
      ObjectId portfolioFileId,
      FeedbackStatus feedbackStatus,
      FeedbackStatus projectStatus,
      int retryCount) {
    return GptFeedbackStatusTopic.builder()
        .id(UUID.randomUUID().toString())
        .userId(userId)
        .portfolioFileId(portfolioFileId)
        .feedbackStatus(feedbackStatus)
        .projectStatus(projectStatus)
        .retryCount(retryCount)
        .build();
  }

  public static GptFeedbackStatusTopic increaseRetryCount(GptFeedbackStatusTopic topic) {
    return GptFeedbackStatusTopic.builder()
        .id(topic.id())
        .userId(topic.userId())
        .portfolioFileId(topic.portfolioFileId())
        .feedbackStatus(topic.feedbackStatus())
        .projectStatus(topic.projectStatus())
        .retryCount(topic.retryCount() + 1)
        .build();
  }
}
