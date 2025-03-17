package depromeet.onepiece.feedback.query.application;

import depromeet.onepiece.feedback.command.presentation.response.RecentFeedbackListResponse;
import depromeet.onepiece.feedback.query.domain.FeedbackQueryRepository;
import depromeet.onepiece.file.command.domain.FileDocumentRepository;
import depromeet.onepiece.file.command.exception.FileNotFoundException;
import depromeet.onepiece.file.domain.FileDocument;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackQueryService {
  private final FeedbackQueryRepository feedbackQueryRepository;
  private final FileDocumentRepository fileRepository;

  public List<RecentFeedbackListResponse> getFeedbackList(String userId) {
    return feedbackQueryRepository.findByUserId(userId).stream()
        .map(
            feedback -> {
              String title = getFileTitle(feedback.getFileId());
              return new RecentFeedbackListResponse(
                  feedback.getId(), feedback.getCreatedAt(), title);
            })
        .toList();
  }

  private String getFileTitle(ObjectId fileId) {
    Optional<FileDocument> file = fileRepository.fileById(fileId);
    return file.map(FileDocument::getLogicalName).orElseThrow(FileNotFoundException::new);
  }
}
