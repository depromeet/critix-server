package depromeet.onepiece.feedback.command.application;

import depromeet.onepiece.feedback.command.domain.FeedbackCommandRepository;
import depromeet.onepiece.feedback.command.infrastructure.ChatGPTService;
import depromeet.onepiece.feedback.command.infrastructure.FeedbackService;
import depromeet.onepiece.feedback.command.presentation.request.ChatGPTRequest;
import depromeet.onepiece.feedback.command.presentation.response.OverallFeedbackResponse;
import depromeet.onepiece.feedback.command.presentation.response.ProjectFeedbackResponse;
import depromeet.onepiece.feedback.command.presentation.response.StartFeedbackResponse;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.FeedbackStatus;
import depromeet.onepiece.file.command.domain.FileDocumentRepository;
import depromeet.onepiece.file.command.exception.FileNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackCommandService {
  private final ChatGPTService chatGPTService;
  private final FileDocumentRepository fileRepository;
  private final FeedbackService feedbackService;
  private final FeedbackCommandRepository feedbackCommandRepository;

  public OverallFeedbackResponse overallFeedback(String portfolioId) {
    // portfolioId 검증
    // portfolioId로 포트폴리오 조회
    // 포트폴리오 이미지 추출
    // chatgptservice의 overallfeedback 포트폴리오 이미지 담아서 호출
    ChatGPTRequest requestDto = createOverallFeedbackRequest(portfolioId);
    return chatGPTService.overallFeedback(requestDto);
  }

  public ProjectFeedbackResponse projectFeedback(String portfolioId) {
    // portfolioId 검증
    // portfolioId로 포트폴리오 조회
    // 포트폴리오 이미지 추출
    // chatgptservice의 projectfeedback 포트폴리오 이미지 담아서 호출
    ChatGPTRequest requestDto = createProjectFeedbackRequest(portfolioId);
    return chatGPTService.projectFeedback(requestDto);
  }

  private ChatGPTRequest createOverallFeedbackRequest(String portfolioId) {
    return new ChatGPTRequest(
        "gpt-4",
        List.of(
            new ChatGPTRequest.Message(
                "user", List.of(new ChatGPTRequest.Message.Content("text", "프롬프트", null)))),
        new ChatGPTRequest.ResponseFormat("json", null),
        0.7,
        5000,
        1.0,
        0,
        0);
  }

  private ChatGPTRequest createProjectFeedbackRequest(String portfolioId) {
    return new ChatGPTRequest(
        "gpt-4",
        List.of(
            new ChatGPTRequest.Message(
                "user", List.of(new ChatGPTRequest.Message.Content("text", "프롬프트", null)))),
        new ChatGPTRequest.ResponseFormat("json", null),
        0.8,
        5000,
        1.0,
        0,
        0);
  }

  public StartFeedbackResponse startFeedback(ObjectId fileId) {
    fileId = fileRepository.fileById(fileId).orElseThrow(FileNotFoundException::new).getId();

    feedbackService.portfolioFeedback(fileId, "");

    Feedback feedback =
        new Feedback(new ObjectId(), null, fileId, FeedbackStatus.IN_PROGRESS, null, null, null);
    Feedback savedFeedback = feedbackCommandRepository.save(feedback);
    return new StartFeedbackResponse(savedFeedback.getId().toString());
  }
}
