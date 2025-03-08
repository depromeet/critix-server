package depromeet.onepiece.feedback.command.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import depromeet.onepiece.file.command.infrastructure.PresignedUrlGenerator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackService {

  private final AzureService azureService;
  private final PresignedUrlGenerator presignedUrlGenerator;
  private final ObjectMapper objectMapper;

  public String portfolioFeedback(String fileId, String additionalChat) {

    List<String> imageUrls = presignedUrlGenerator.generatePresignedUrl(fileId);

    String overallJsonSchema = ChatGPTConstants.OverallSchema;
    String projectJsonSchema = ChatGPTConstants.ProjectSchema;

    String overallFeedback =
        azureService.processChat(
            imageUrls, ChatGPTConstants.OVERALL_PROMPT, additionalChat, overallJsonSchema);
    String projectFeedback =
        azureService.processChat(
            imageUrls, ChatGPTConstants.PROJECT_PROMPT, additionalChat, projectJsonSchema);
    try {
      JsonNode overallJsonNode = objectMapper.readTree(overallFeedback);
      JsonNode projectJsonNode = objectMapper.readTree(projectFeedback);

      Map<String, JsonNode> mergedResponse = new HashMap<>();
      mergedResponse.put("overallEvaluation", overallJsonNode);
      mergedResponse.put("projectEvaluation", projectJsonNode);

      String mergedJson = objectMapper.writeValueAsString(mergedResponse);
      return mergedJson;

    } catch (Exception e) {
      log.error("JSON 병합 중 오류 발생", e);
      return "{\"error\":\"JSON 병합 실패\"}";
    }
  }
}
