package depromeet.onepiece.common.config;

import depromeet.onepiece.portfolio.command.infrastructure.ChatGPTProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ChatGPTProperties.class})
public class PropertiesConfig {}
