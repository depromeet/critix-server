package depromeet.onepiece.file.query.infrastructure;

import depromeet.onepiece.file.domain.FileDocument;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileQueryRepositoryImpl implements FileQueryRepository {

  private final FileQueryMongoRepository fileQueryMongoRepository;

  @Override
  public List<FileDocument> findAllByIds(List<ObjectId> idList) {
    return fileQueryMongoRepository.findAllById(idList);
  }
}
