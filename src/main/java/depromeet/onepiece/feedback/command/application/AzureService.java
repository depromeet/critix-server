package depromeet.onepiece.feedback.command.application;

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
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.core.util.BinaryData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import depromeet.onepiece.common.utils.ConvertService;
import depromeet.onepiece.common.utils.RedisPrefix;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AzureService {
  private final ChatGPTProperties chatGPTProperties;
  private OpenAIClient client;
  private final ObjectMapper objectMapper;
  private final RedisTemplate<String, String> redisTemplate;

  @PostConstruct
  private void initClient() {
    String apiKey = chatGPTProperties.apiKey();
    this.client =
        new OpenAIClientBuilder()
            .credential(new KeyCredential(apiKey))
            .httpClient(
                new NettyAsyncHttpClientBuilder()
                    .connectTimeout(Duration.ofMillis(600000))
                    .readTimeout(java.time.Duration.ofSeconds(500))
                    .build())
            .buildClient();
  }

  public String processChat(
      List<String> imageUrls, String prompt, String jsonSchema, String ocrResult) {
    List<ChatRequestMessage> chatMessages = new ArrayList<>();

    chatMessages.add(new ChatRequestSystemMessage(prompt));

    JsonNode jsonNode = ConvertService.readTree(ocrResult);
    int maxSize = 50;
    List<ChatMessageContentItem> messageContent = new ArrayList<>();

    Boolean imageFlag = redisTemplate.hasKey(RedisPrefix.AI_INCLUDE_IMAGE_FLAG);
    for (int i = 0; i < jsonNode.size(); i++) {
      messageContent.add(
          new ChatMessageTextContentItem(jsonNode.findValue((i + 1) + ".png").toString()));
      if (imageFlag) {
        messageContent.add(
            new ChatMessageImageContentItem(new ChatMessageImageUrl(imageUrls.get(i))));
      }
      ;
    }

    chatMessages.add(new ChatRequestUserMessage(messageContent));

    ChatCompletionsOptions chatCompletionsOptions =
        new ChatCompletionsOptions(chatMessages)
            .setResponseFormat(
                new ChatCompletionsJsonSchemaResponseFormat(
                    new ChatCompletionsJsonSchemaResponseFormatJsonSchema("get_weather")
                        .setStrict(true)
                        .setDescription("디자이너의 포트폴리오(이미지 순서대로 1페이지, 2페이지, 3페이지..)")
                        .setSchema(BinaryData.fromString(jsonSchema))));

    try {
      ChatCompletions chatCompletions = client.getChatCompletions("gpt-4o", chatCompletionsOptions);
      Map<String, Object> result = new HashMap<>();
      result.put("id", chatCompletions.getId());
      result.put("choices", chatCompletions.getChoices());
      result.put("usage", chatCompletions.getUsage());
      result.put("model", chatCompletions.getModel());
      result.put("systemFingerprint", chatCompletions.getSystemFingerprint());
      result.put("promptFilterResults", chatCompletions.getPromptFilterResults());
      result.put("createdAt", chatCompletions.getCreatedAt().toString());
      log.info("Chat response: {}", result);

      ObjectMapper objectMapper = new ObjectMapper();
      String jsonResponse =
          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);

      log.info("Chat response: {}", jsonResponse);
      return chatCompletions.getChoices().stream()
          .map(choice -> choice.getMessage().getContent())
          .collect(Collectors.joining("\n"));
    } catch (Exception e) {
      log.error("Azure OpenAI API 호출 중 오류 발생", e);
      throw new RuntimeException("OpenAI API 호출 실패", e);
    }
  }
}
