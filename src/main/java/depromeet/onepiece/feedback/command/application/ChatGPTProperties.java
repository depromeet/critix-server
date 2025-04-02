package depromeet.onepiece.feedback.command.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chatgpt")
public record ChatGPTProperties(
    String apiKey,
    String overallAssistantId,
    String projectAssistantId,
    String model,
    Double temperature,
    Integer maxTokens) {}
