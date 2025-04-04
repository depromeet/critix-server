package depromeet.onepiece.feedback.command.application;

import depromeet.onepiece.common.utils.EncryptionUtil;
import depromeet.onepiece.feedback.command.infrastructure.FeedbackCommandRepository;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.FeedbackStatus;
import depromeet.onepiece.file.command.infrastructure.FileDocumentRepository;
import depromeet.onepiece.file.domain.FileDocument;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackCommandService {
  private final FeedbackCommandRepository feedbackCommandRepository;
  private final FileDocumentRepository fileDocumentRepository;

  public Feedback save(Feedback feedback) {
    return feedbackCommandRepository.save(feedback);
  }

  public ObjectId saveEmpty(ObjectId userId, ObjectId fileId) {
    Feedback feedback = Feedback.saveEmptyFeedback(userId, fileId);
    Optional<FileDocument> fileDocument = fileDocumentRepository.findById(fileId);
    String title = EncryptionUtil.decrypt(fileDocument.get().getLogicalName());
    feedback.updateTitle(title);
    return feedbackCommandRepository.save(feedback).getId();
  }

  public boolean updateOverallStatus(ObjectId feedbackId, FeedbackStatus feedbackStatus) {

    return feedbackCommandRepository.updateStatus(feedbackId, feedbackStatus, "overall_status");
  }

  public boolean updateProjectStatus(ObjectId feedbackId, FeedbackStatus feedbackStatus) {

    return feedbackCommandRepository.updateStatus(feedbackId, feedbackStatus, "project_status");
  }
}
