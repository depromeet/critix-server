package depromeet.onepiece.common.eventsourcing.config;

import static org.apache.kafka.common.requests.CreateTopicsRequest.NO_REPLICATION_FACTOR;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConfig {

  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  @Bean
  public NewTopic topic1() {
    return new NewTopic(topic, 3, NO_REPLICATION_FACTOR);
  }

  @Bean
  public DefaultErrorHandler errorHandler() {
    BackOff fixedBackOff = new FixedBackOff(1000, 5);
    return new DefaultErrorHandler(
        (consumerRecord, exception) -> {
          log.error("Error Data: {}", consumerRecord.toString());
        },
        fixedBackOff);
  }
}
