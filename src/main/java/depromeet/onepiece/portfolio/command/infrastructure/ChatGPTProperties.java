package depromeet.onepiece.portfolio.command.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chatgpt")
public record ChatGPTProperties(
    String apiKey, String model, Double temperature, Integer maxTokens) {}
