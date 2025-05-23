package depromeet.onepiece.file.command.infrastructure;

import depromeet.onepiece.file.domain.FileDocument;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileDocumentRepositoryImpl implements FileDocumentRepository {
  private final FileDocumentMongoRepository fileDocumentMongoRepository;

  @Override
  public FileDocument save(FileDocument fileDocument) {
    return fileDocumentMongoRepository.save(fileDocument);
  }

  @Override
  public Optional<FileDocument> findById(ObjectId id) {
    return fileDocumentMongoRepository.findById(id);
  }
}
