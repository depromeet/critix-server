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
}
