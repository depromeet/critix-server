package depromeet.onepiece.feedback.command.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import depromeet.onepiece.feedback.command.infrastructure.FeedbackCommandRepository;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.OverallEvaluation;
import depromeet.onepiece.feedback.domain.ProjectEvaluation;
import depromeet.onepiece.file.command.infrastructure.PresignedUrlGenerator;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackService {

  private final AzureService azureService;
  private final PresignedUrlGenerator presignedUrlGenerator;
  private final FeedbackCommandRepository feedbackCommandRepository;
  private final ObjectMapper objectMapper;

  public Feedback portfolioFeedback(String fileId, String additionalChat) {

    List<String> imageUrls = presignedUrlGenerator.generatePresignedUrl(fileId);

    String overallJsonSchema = ChatGPTConstants.OverallSchema;
    String projectJsonSchema = ChatGPTConstants.ProjectSchema;

    String overallFeedback =
        azureService.processChat(
            imageUrls, ChatGPTConstants.OVERALL_PROMPT, additionalChat, overallJsonSchema);
    try { // 임시로 슬립 1분
      Thread.sleep(60 * 1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    String projectFeedback =
        azureService.processChat(
            imageUrls, ChatGPTConstants.PROJECT_PROMPT, additionalChat, projectJsonSchema);
    try {
      OverallEvaluation overallEvaluation =
          objectMapper.readValue(overallFeedback, OverallEvaluation.class);
      JsonNode projectJsonNode =
          objectMapper.readTree(projectFeedback).findPath("projectEvaluation");
      List<ProjectEvaluation> projectEvaluations =
          objectMapper.convertValue(
              projectJsonNode, new TypeReference<List<ProjectEvaluation>>() {});

      Feedback feedback =
          new Feedback(
              new ObjectId(),
              null, // TODO user id 넣기,
              new ObjectId(fileId),
              overallEvaluation,
              Collections.emptyList(), // 처음에는 채팅 내용이 없다.
              projectEvaluations);

      feedbackCommandRepository.save(feedback);
      return feedback;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
