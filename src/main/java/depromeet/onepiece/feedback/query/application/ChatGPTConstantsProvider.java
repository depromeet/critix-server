package depromeet.onepiece.feedback.query.application;

import depromeet.onepiece.feedback.domain.ChatGPTConstants;
import depromeet.onepiece.feedback.query.application.exception.ChatGPTConstantNotFoundException;
import depromeet.onepiece.feedback.query.infrastructure.ChatGPTConstantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatGPTConstantsProvider {

  private final ChatGPTConstantsRepository chatGPTConstantsRepository;

  public ChatGPTConstants getConstants() {
    return chatGPTConstantsRepository
        .findTopByOrderByCreatedAtDesc()
        .orElseThrow(ChatGPTConstantNotFoundException::new);
  }

  public String getOverallPrompt() {
    return getConstants().getOverallPrompt();
  }

  public String getOverallSchema() {
    return getConstants().getOverallSchema();
  }

  public String getProjectPrompt() {
    return getConstants().getProjectPrompt();
  }

  public String getProjectSchema() {
    return getConstants().getProjectSchema();
  }

  public String getFilteringPrompt() {
    return "이게 디자이너의 포트폴리오인지 true, false로 판단해줘. ";
  }

  public String getFilteringSchema() {
    return "{\n"
        + "    \"type\": \"object\",\n"
        + "    \"properties\": {\n"
        + "      \"response\": {\n"
        + "        \"type\": \"boolean\",\n"
        + "        \"description\": \"A boolean value indicating true or false.\"\n"
        + "      }\n"
        + "    },\n"
        + "    \"required\": [\n"
        + "      \"response\"\n"
        + "    ],\n"
        + "    \"additionalProperties\": false\n"
        + "  }";
  }
}
