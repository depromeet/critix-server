package depromeet.onepiece.feedback.command.application;

import static depromeet.onepiece.feedback.command.application.ChatGPTConstants.OverallSchema;
import static depromeet.onepiece.feedback.command.application.ChatGPTConstants.ProjectSchema;
import static depromeet.onepiece.feedback.domain.FeedbackStatus.COMPLETE;
import static depromeet.onepiece.feedback.domain.FeedbackStatus.PENDING;

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
import depromeet.onepiece.feedback.query.application.FeedbackQueryService;
import depromeet.onepiece.file.command.infrastructure.PresignedUrlGenerator;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackCommandFacadeService {

  private final AzureService azureService;
  private final PresignedUrlGenerator presignedUrlGenerator;
  private final FeedbackCommandService feedbackCommandService;
  private final FeedbackQueryService feedbackQueryService;
  private final GPTEventProducer gptEventProducer;

  @Transactional
  public void requestEvaluation(final ObjectId feedbackId, final ObjectId fileId) {
    Feedback feedback = feedbackQueryService.getById(feedbackId);
    feedback.updateStatusInProgress();

    List<String> imageUrls = presignedUrlGenerator.generatePresignedUrl(fileId.toString());

    requestOverallEvaluation(imageUrls, feedback);
    requestProjectEvaluation(imageUrls, feedback);
  }

  private void requestProjectEvaluation(List<String> imageUrls, Feedback feedback) {
    if (feedback.getProjectStatus() == COMPLETE) return;
    String projectFeedback =
        azureService.processChat(imageUrls, ChatGPTConstants.PROJECT_PROMPT, ProjectSchema);
    JsonNode projectJsonNode = ConvertService.readTree(projectFeedback, "projectEvaluation");
    feedback.completeProjectEvaluation(
        ConvertService.convertValue(
            projectJsonNode, new TypeReference<List<ProjectEvaluation>>() {}));
  }

  private void requestOverallEvaluation(List<String> imageUrls, Feedback feedback) {
    if (feedback.getProjectStatus() == COMPLETE) return;
    String overallFeedback =
        azureService.processChat(imageUrls, ChatGPTConstants.OVERALL_PROMPT, OverallSchema);
    feedback.completeOverallEvaluation(
        ConvertService.readValue(overallFeedback, OverallEvaluation.class));
  }

  public StartFeedbackResponse startFeedback(ObjectId userId, ObjectId fileId) {
    ObjectId feedbackId = feedbackCommandService.saveEmpty(userId, fileId);
    log.info("feedbackId: {}", feedbackId);
    GPTFeedbackStatusTopic gptFeedbackStatusTopic =
        GPTFeedbackStatusTopic.of(userId, fileId, feedbackId, PENDING, PENDING, 0);
    log.info("gptFeedbackStatusTopic: {}", gptFeedbackStatusTopic);
    gptEventProducer.produceTopic(gptFeedbackStatusTopic);

    return new StartFeedbackResponse(feedbackId.toString());
  }

  /**
   * TODO 카프카 연동하기 , 이 메서드 호출해서 ai 요청하기, 추후 수정 여지가 매우 많음 .일단 이러한 형태로 진행이 될거고 중간중간 에러 핸들링이나 상태 저장등
   * 해야됌.
   *
   * @param userId 사용자 id
   * @param fileId 포폴 파일 id
   * @return 생성된 완전한 피드백 객체.
   */
  public Feedback portfolioFeedback(ObjectId userId, ObjectId fileId) {

    List<String> imageUrls = presignedUrlGenerator.generatePresignedUrl(fileId.toString());

    String overallJsonSchema = OverallSchema;
    String projectJsonSchema = ProjectSchema;

    String overallFeedback =
        azureService.processChat(imageUrls, ChatGPTConstants.OVERALL_PROMPT, overallJsonSchema);
    String projectFeedback =
        azureService.processChat(imageUrls, ChatGPTConstants.PROJECT_PROMPT, projectJsonSchema);

    OverallEvaluation overallEvaluation =
        ConvertService.readValue(overallFeedback, OverallEvaluation.class);
    JsonNode projectJsonNode = ConvertService.readTree(projectFeedback, "projectEvaluation");
    List<ProjectEvaluation> projectEvaluations =
        ConvertService.convertValue(
            projectJsonNode, new TypeReference<List<ProjectEvaluation>>() {});

    Feedback feedback =
        new Feedback(
            new ObjectId(),
            userId,
            fileId,
            null,
            FeedbackStatus.PENDING,
            FeedbackStatus.PENDING,
            overallEvaluation,
            Collections.emptyList(), // 처음에는 채팅 내용이 없다.
            projectEvaluations);

    feedbackCommandService.save(feedback);
    return feedback;
  }
}
