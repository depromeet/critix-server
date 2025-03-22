package depromeet.onepiece.file.query.application;

import depromeet.onepiece.file.domain.FileDocument;
import depromeet.onepiece.file.query.infrastructure.FileQueryRepository;
import java.util.List;
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
}
