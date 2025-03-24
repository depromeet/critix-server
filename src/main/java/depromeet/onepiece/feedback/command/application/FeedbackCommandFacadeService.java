package depromeet.onepiece.feedback.command.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import depromeet.onepiece.common.utils.ConvertService;
import depromeet.onepiece.feedback.command.presentation.response.StartFeedbackResponse;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.FeedbackStatus;
import depromeet.onepiece.feedback.domain.OverallEvaluation;
import depromeet.onepiece.feedback.domain.ProjectEvaluation;
import depromeet.onepiece.file.command.infrastructure.PresignedUrlGenerator;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackCommandFacadeService {

  private final AzureService azureService;
  private final PresignedUrlGenerator presignedUrlGenerator;
  private final FeedbackCommandService feedbackCommandService;

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

    String overallJsonSchema = ChatGPTConstants.OverallSchema;
    String projectJsonSchema = ChatGPTConstants.ProjectSchema;

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

  public StartFeedbackResponse startFeedback(ObjectId userId, ObjectId fileId) {
    Feedback feedback = feedbackCommandService.saveEmpty(userId, fileId);
    return new StartFeedbackResponse(feedback.getId().toString());
  }
}
