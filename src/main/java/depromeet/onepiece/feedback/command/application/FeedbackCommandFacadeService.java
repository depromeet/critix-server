package depromeet.onepiece.feedback.command.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import depromeet.onepiece.common.eventsourcing.dto.GPTFeedbackStatusTopic;
import depromeet.onepiece.common.eventsourcing.producer.GPTEventProducer;
import depromeet.onepiece.common.utils.ConvertService;
import depromeet.onepiece.feedback.command.application.exception.OCRResultNotFoundException;
import depromeet.onepiece.feedback.command.infrastructure.S3OCRJsonPoller;
import depromeet.onepiece.feedback.command.presentation.response.StartFeedbackResponse;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.FeedbackStatus;
import depromeet.onepiece.feedback.domain.OverallEvaluation;
import depromeet.onepiece.feedback.domain.ProjectEvaluation;
import depromeet.onepiece.feedback.query.application.ChatGPTConstantsProvider;
import depromeet.onepiece.feedback.query.application.FeedbackQueryService;
import depromeet.onepiece.file.command.application.PresignedUrlGenerator;
import depromeet.onepiece.user.domain.User;
import depromeet.onepiece.user.query.domain.UserQueryRepository;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
  private final S3OCRJsonPoller s3ocrJsonPoller;
  private final UserQueryRepository userQueryRepository;

  // @Transactional
  public void requestEvaluation(final ObjectId feedbackId, final ObjectId fileId) {
    Feedback feedback = feedbackQueryService.getById(feedbackId);
    log.info("피드백 요청 메서드 호출");
    String ocrResult = "";
    try {
      log.info("OCR 결과 요청 file Id: {}", fileId);
      ocrResult = s3ocrJsonPoller.waitForResult(feedback.getFileId().toString(), 30000, 1000);
      log.info("OCR 결과 수신 완료: {}", ocrResult);
    } catch (OCRResultNotFoundException e) {
      log.warn("OCR 결과를 시간 내에 찾을 수 없습니다.");
    }

    List<String> imageUrls = presignedUrlGenerator.generatePresignedUrl(fileId.toString());

    ObjectId userId = feedback.getUserId();
    Optional<User> user = userQueryRepository.findUserById(userId);
    String userName = user.map(User::getName).orElse("디자이너");

    requestOverallEvaluation(imageUrls, feedback, ocrResult, userName);
    requestProjectEvaluation(imageUrls, feedback, ocrResult);
  }

  public boolean requestPortfolioFiltering(ObjectId fileId) {
    String ocrResult = s3ocrJsonPoller.waitForResult(fileId.toString(), 30000, 1000);
    List<String> imageUrls = presignedUrlGenerator.generatePresignedUrl(fileId.toString());

    String portfolioFilteringPrompt = chatGPTConstantsProvider.getFilteringPrompt() + ocrResult;
    String portfolioFilterSchema = chatGPTConstantsProvider.getFilteringSchema();

    log.info("필터링 프롬프트: {}, 스키마: {}", portfolioFilteringPrompt, portfolioFilterSchema);

    String filterPortfolio =
        azureService.processChat(imageUrls, portfolioFilteringPrompt, portfolioFilterSchema);
    log.info("필터링 결과: {}", filterPortfolio);

    JsonNode projectJsonNode = ConvertService.readTree(filterPortfolio, "response");
    if (projectJsonNode == null || projectJsonNode.isNull()) {
      log.warn("필터링 결과 파싱 실패: 응답에 'response' 필드가 없습니다. 원본 응답: {}", filterPortfolio);
      return false;
    }

    String resultText = projectJsonNode.asText("").trim().toLowerCase(Locale.ROOT);
    boolean isPortfolio = resultText.equals("true") || resultText.equals("\"true\"");
    log.info("포트폴리오 여부: {}", isPortfolio);

    return isPortfolio;
  }

  private void requestProjectEvaluation(
      List<String> imageUrls, Feedback feedback, String ocrResult) {
    if (feedback.getProjectStatus() == FeedbackStatus.COMPLETE) {
      return;
    }
    String projectPrompt =
        chatGPTConstantsProvider.getProjectPrompt()
            + "\n\n"
            + "포트폴리오 각 페이지의 내용(이걸 이용해 페이지 별 피드백을 작성하고 pageNumber 칸에 파일명(ex. 3.png)를 넣어줘:\n"
            + ocrResult;
    feedbackCommandService.updateProjectStatus(feedback.getId(), FeedbackStatus.IN_PROGRESS);
    String projectFeedback =
        azureService.processChat(
            imageUrls, projectPrompt, chatGPTConstantsProvider.getProjectSchema());
    feedbackCommandService.updateProjectStatus(feedback.getId(), FeedbackStatus.COMPLETE);

    JsonNode projectJsonNode = ConvertService.readTree(projectFeedback, "projectEvaluation");
    feedback.completeProjectEvaluation(
        ConvertService.convertValue(
            projectJsonNode, new TypeReference<List<ProjectEvaluation>>() {}));
    feedbackCommandService.save(feedback);
  }

  private void requestOverallEvaluation(
      List<String> imageUrls, Feedback feedback, String ocrResult, String userName) {
    if (feedback.getOverallStatus() == FeedbackStatus.COMPLETE) {
      return;
    }
    String overallPrompt =
        chatGPTConstantsProvider.getOverallPrompt()
            + "\n\n"
            + "포트폴리오 각 페이지의 내용은 다음과 같아:\n"
            + ocrResult
            + "디자이너의 이름(userName)은 다음과 같아:\n"
            + userName;
    feedbackCommandService.updateOverallStatus(feedback.getId(), FeedbackStatus.IN_PROGRESS);
    String overallFeedback =
        azureService.processChat(
            imageUrls, overallPrompt, chatGPTConstantsProvider.getOverallSchema());
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
