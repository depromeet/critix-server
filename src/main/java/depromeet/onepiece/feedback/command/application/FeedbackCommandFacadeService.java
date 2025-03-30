package depromeet.onepiece.feedback.command.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import depromeet.onepiece.common.eventsourcing.dto.GPTFeedbackStatusTopic;
import depromeet.onepiece.common.eventsourcing.producer.GPTEventProducer;
import depromeet.onepiece.common.utils.ConvertService;
import depromeet.onepiece.feedback.command.presentation.response.StartFeedbackResponse;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.FeedbackStatus;
import depromeet.onepiece.feedback.domain.OverallEvaluation;
import depromeet.onepiece.feedback.domain.ProjectEvaluation;
import depromeet.onepiece.feedback.query.application.ChatGPTConstantsProvider;
import depromeet.onepiece.feedback.query.application.FeedbackQueryService;
import depromeet.onepiece.file.command.infrastructure.PresignedUrlGenerator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackCommandFacadeService {

  private final AzureService azureService;
  private final PresignedUrlGenerator presignedUrlGenerator;
  private final FeedbackCommandService feedbackCommandService;
  private final FeedbackQueryService feedbackQueryService;
  private final GPTEventProducer gptEventProducer;
  private final ChatGPTConstantsProvider chatGPTConstantsProvider;

  // @Transactional
  public void requestEvaluation(final ObjectId feedbackId, final ObjectId fileId) {
    Feedback feedback = feedbackQueryService.getById(feedbackId);

    List<String> imageUrls = presignedUrlGenerator.generatePresignedUrl(fileId.toString());

    requestOverallEvaluation(imageUrls, feedback);
    requestProjectEvaluation(imageUrls, feedback);
  }

  private void requestProjectEvaluation(List<String> imageUrls, Feedback feedback) {
    if (feedback.getProjectStatus() == FeedbackStatus.COMPLETE) {
      return;
    }
    feedbackCommandService.updateProjectStatus(feedback.getId(), FeedbackStatus.IN_PROGRESS);
    String projectFeedback =
        azureService.processChat(
            imageUrls,
            chatGPTConstantsProvider.getProjectPrompt(),
            chatGPTConstantsProvider.getProjectSchema());
    feedbackCommandService.updateProjectStatus(feedback.getId(), FeedbackStatus.COMPLETE);

    JsonNode projectJsonNode = ConvertService.readTree(projectFeedback, "projectEvaluation");
    feedback.completeProjectEvaluation(
        ConvertService.convertValue(
            projectJsonNode, new TypeReference<List<ProjectEvaluation>>() {}));
    feedbackCommandService.save(feedback);
  }

  private void requestOverallEvaluation(List<String> imageUrls, Feedback feedback) {
    if (feedback.getProjectStatus() == FeedbackStatus.COMPLETE) {
      return;
    }
    feedbackCommandService.updateOverallStatus(feedback.getId(), FeedbackStatus.IN_PROGRESS);
    String overallFeedback =
        azureService.processChat(
            imageUrls,
            chatGPTConstantsProvider.getOverallPrompt(),
            chatGPTConstantsProvider.getOverallSchema());
    feedbackCommandService.updateOverallStatus(feedback.getId(), FeedbackStatus.COMPLETE);
    feedback.completeOverallEvaluation(
        ConvertService.readValue(overallFeedback, OverallEvaluation.class));
    feedbackCommandService.save(feedback);
  }

  public StartFeedbackResponse startFeedback(ObjectId userId, ObjectId fileId) {
    ObjectId feedbackId = feedbackCommandService.saveEmpty(userId, fileId);

    GPTFeedbackStatusTopic gptFeedbackStatusTopic =
        GPTFeedbackStatusTopic.of(
            userId, fileId, feedbackId, FeedbackStatus.PENDING, FeedbackStatus.PENDING, 0);
    log.info("topic: {}", gptFeedbackStatusTopic);

    gptEventProducer.produceTopic(gptFeedbackStatusTopic);
    return new StartFeedbackResponse(feedbackId.toString());
  }
}
