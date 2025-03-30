package depromeet.onepiece.common.eventsourcing.consumer;

import depromeet.onepiece.common.eventsourcing.dto.GPTFeedbackStatusTopic;
import depromeet.onepiece.feedback.command.application.FeedbackCommandFacadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTEventConsumer {
  private final FeedbackCommandFacadeService feedbackCommandFacadeService;

  @KafkaListener(
      topics = "gpt-limiter",
      clientIdPrefix = "topic-listener",
      groupId = "${spring.kafka.consumer.group-id}")
  public void consumeFeedbackEvent(
      final GPTFeedbackStatusTopic topic, ConsumerRecordMetadata metadata) {
    log.info("Received Kafka message: {}", topic);
    log.info(
        "Topic: {}, Partition: {}, Offset: {}, Timestamp: {}",
        metadata.topic(),
        metadata.partition(),
        metadata.offset(),
        metadata.timestamp());

    // TODO: 필요 시 resilience4j 같은 라이브러리 사용
    feedbackCommandFacadeService.requestEvaluation(
        new ObjectId(topic.feedbackId()), new ObjectId(topic.fileId()));
  }
}
