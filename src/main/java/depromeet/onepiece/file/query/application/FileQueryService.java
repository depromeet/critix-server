package depromeet.onepiece.file.query.application;

import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.file.domain.FileDocument;
import depromeet.onepiece.file.query.infrastructure.FileQueryRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileQueryService {
  private final FileQueryRepository fileQueryRepository;

  public List<FileDocument> findAllByIds(List<ObjectId> fileIdList) {
    return fileQueryRepository.findAllByIds(fileIdList);
  }

  public Map<ObjectId, FileDocument> getFileMapByFeedback(List<Feedback> feedbackList) {
    List<ObjectId> fileIdList = feedbackList.stream().map(Feedback::getFileId).toList();
    return fileQueryRepository.findAllByIds(fileIdList).stream()
        .collect(Collectors.toMap(FileDocument::getId, Function.identity()));
  }
}
