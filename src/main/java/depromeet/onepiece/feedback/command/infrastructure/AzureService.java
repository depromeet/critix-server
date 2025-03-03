package depromeet.onepiece.feedback.command.infrastructure;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsJsonSchemaResponseFormat;
import com.azure.ai.openai.models.ChatCompletionsJsonSchemaResponseFormatJsonSchema;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessageContentItem;
import com.azure.ai.openai.models.ChatMessageImageContentItem;
import com.azure.ai.openai.models.ChatMessageImageUrl;
import com.azure.ai.openai.models.ChatMessageTextContentItem;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.azure.core.credential.KeyCredential;
import com.azure.core.util.BinaryData;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.security.auth.login.Configuration.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AzureService {
  private final ChatGPTProperties chatGPTProperties;
  private OpenAIClient client;

  @PostConstruct
  private void initClient() {
    String apiKey = chatGPTProperties.apiKey();
    this.client = new OpenAIClientBuilder().credential(new KeyCredential(apiKey)).buildClient();
  }

  public void processChat(List<String> imageUrls, String prompt, String additionalChat) {
    if (imageUrls == null || imageUrls.isEmpty()) {
      log.error("No image URLs provided.");
      return;
    }

    List<ChatRequestMessage> chatMessages = new ArrayList<>();

    chatMessages.add(new ChatRequestSystemMessage(prompt));

    List<ChatMessageContentItem> messageContent = new ArrayList<>();
    messageContent.add(new ChatMessageTextContentItem(additionalChat));

    messageContent.addAll(
        imageUrls.stream()
            .limit(100) // 최대 100개 제한
            .map(url -> new ChatMessageImageContentItem(new ChatMessageImageUrl(url)))
            .collect(Collectors.toList()));

    chatMessages.add(new ChatRequestUserMessage(messageContent));

    // json 응답 지정 (structured output)
    ChatCompletionsOptions chatCompletionsOptions =
        new ChatCompletionsOptions(chatMessages)
            .setResponseFormat(
                new ChatCompletionsJsonSchemaResponseFormat(
                    new ChatCompletionsJsonSchemaResponseFormatJsonSchema("get_weather")
                        .setStrict(true)
                        .setDescription("Fetches the weather in the given location")
                        .setSchema(BinaryData.fromObject(new Parameters() {})) // JSON 스키마 적용
                    ));

    // api 호출
    ChatCompletions chatCompletions =
        client.getChatCompletions("{chatgpt모델명}", chatCompletionsOptions);
    log.info("Chat response: {}", chatCompletions);
  }
}
