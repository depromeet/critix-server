package depromeet.onepiece.feedback.query.application;

import depromeet.onepiece.common.utils.EncryptionUtil;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.query.presentation.response.RecentFeedbackListResponse;
import depromeet.onepiece.file.domain.FileDocument;
import depromeet.onepiece.file.query.application.FileQueryService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackQueryFacadeService {
  private final FeedbackQueryService feedbackQueryService;
  private final FileQueryService fileQueryService;

  public List<RecentFeedbackListResponse> getFeedbackList(ObjectId userId) {
    List<Feedback> feedbackList = feedbackQueryService.findByUserId(userId);
    List<ObjectId> fileIdList = feedbackList.stream().map(Feedback::getFileId).toList();
    Map<ObjectId, FileDocument> fileMap =
        fileQueryService.findAllByIds(fileIdList).stream()
            .collect(Collectors.toMap(FileDocument::getId, Function.identity()));
    return feedbackList.stream()
        .map(
            feedback -> {
              String title = getFileTitle(feedback.getFileId(), fileMap);
              return new RecentFeedbackListResponse(
                  feedback.getId(), feedback.getCreatedAt().toLocalDate(), title);
            })
        .toList();
  }

  private String getFileTitle(ObjectId fileId, Map<ObjectId, FileDocument> fileMap) {
    return Optional.ofNullable(fileMap.get(fileId))
        .map(fileDocument -> EncryptionUtil.decrypt(fileDocument.getLogicalName()))
        .orElse("");
  }
}
