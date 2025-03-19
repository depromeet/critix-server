package depromeet.onepiece.file.command.domain;

import depromeet.onepiece.file.domain.FileDocument;
import java.util.Optional;
import org.bson.types.ObjectId;

public interface FileDocumentRepository {
  FileDocument save(FileDocument fileDocument);

  Optional<FileDocument> fileById(ObjectId fileId);
}
