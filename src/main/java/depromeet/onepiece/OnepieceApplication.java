package depromeet.onepiece;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "depromeet.onepiece")
public class OnepieceApplication {

  public static void main(String[] args) {
    SpringApplication.run(OnepieceApplication.class, args);
  }
}
