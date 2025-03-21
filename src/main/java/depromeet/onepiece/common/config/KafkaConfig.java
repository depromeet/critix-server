package depromeet.onepiece.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.requests.CreateTopicsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaConfig {

  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  @Bean
  public NewTopic topic1() {
    return new NewTopic(topic, 3, CreateTopicsRequest.NO_REPLICATION_FACTOR);
  }
}
