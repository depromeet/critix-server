package depromeet.onepiece.feedback.command.infrastructure;

import com.azure.ai.openai.assistants.AssistantsClient;
import com.azure.ai.openai.assistants.AssistantsClientBuilder;
import com.azure.ai.openai.assistants.models.AssistantThread;
import com.azure.ai.openai.assistants.models.AssistantThreadCreationOptions;
import com.azure.ai.openai.assistants.models.CreateRunOptions;
import com.azure.ai.openai.assistants.models.MessageRole;
import com.azure.ai.openai.assistants.models.MessageTextContent;
import com.azure.ai.openai.assistants.models.MessageTextDetails;
import com.azure.ai.openai.assistants.models.RunStatus;
import com.azure.ai.openai.assistants.models.ThreadMessage;
import com.azure.ai.openai.assistants.models.ThreadMessageOptions;
import com.azure.ai.openai.assistants.models.ThreadRun;
import com.azure.core.credential.KeyCredential;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AzureService {

  private final ChatGPTProperties chatGPTProperties;
  private final AssistantsClient client;

  public AzureService(ChatGPTProperties chatGPTProperties) {
    this.chatGPTProperties = chatGPTProperties;
    this.client =
        new AssistantsClientBuilder()
            .credential(new KeyCredential(chatGPTProperties.apiKey()))
            .buildClient();
  }

  public Map<String, String> processAssistantRequest() {
    Map<String, String> response = new HashMap<>();

    // 새로운 스레드 생성
    String threadId = createThread();
    ThreadRun run = executeAssistant(threadId);
    if (run == null) {
      response.put("status", "failed");
      return response;
    }

    String assistantResponse = getAssistantResponse(threadId);
    response.put("status", "completed");
    response.put("assistant_response", assistantResponse);
    return response;
  }

  private String createThread() {
    AssistantThread thread = client.createThread(new AssistantThreadCreationOptions());
    return thread.getId();
  }

  // 추가 질문
  private void sendMessageToThread(String threadId, String userMessage) {
    client.createMessage(threadId, new ThreadMessageOptions(MessageRole.USER, userMessage));
  }

  private ThreadRun executeAssistant(String threadId) {
    ThreadRun run =
        client.createRun(threadId, new CreateRunOptions(chatGPTProperties.overallAssistantId()));
    log.info("Run ID: {}", run.getId());

    while (true) {
      run = client.getRun(threadId, run.getId());
      log.info("Run status: {}", run.getStatus());

      if (run.getStatus() == RunStatus.COMPLETED) {
        log.info("Assistant response is ready!");
        return run;
      } else if (run.getStatus() == RunStatus.FAILED) {
        log.error("Assistant execution failed!");
        return null;
      }

      try {
        Thread.sleep(2000); // 2초 대기 후 상태 재확인
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  private String getAssistantResponse(String threadId) {
    List<ThreadMessage> messages = client.listMessages(threadId).getData();
    if (messages.isEmpty()) {
      log.warn("No response from Assistant.");
      return "No response from Assistant.";
    }

    return messages.stream()
        .map(ThreadMessage::getContent)
        .flatMap(List::stream)
        .filter(MessageTextContent.class::isInstance)
        .map(MessageTextContent.class::cast)
        .map(this::extractText)
        .collect(Collectors.joining("\n"));
  }

  private String extractText(MessageTextContent content) {
    if (content == null) {
      return "";
    }
    if (content.getText() instanceof MessageTextDetails) {
      return ((MessageTextDetails) content.getText()).getValue();
    }
    return "";
  }
}
