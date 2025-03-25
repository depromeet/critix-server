package depromeet.onepiece.common.eventsourcing.producer;

import depromeet.onepiece.common.eventsourcing.dto.GPTFeedbackStatusTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTEventProducer {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  public void produceTopic(final GPTFeedbackStatusTopic topicDto) {
    kafkaTemplate
        .send(topic, topicDto.id(), topicDto)
        .whenComplete(
            (result, throwable) -> {
              if (throwable != null) {
                log.error("fail to send message, {}", throwable.getMessage());
              } else {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info(
                    "message sent to topic: {}, partition: {}, offset: {}",
                    metadata.topic(),
                    metadata.partition(),
                    metadata.offset());
              }
            });
  }
}
