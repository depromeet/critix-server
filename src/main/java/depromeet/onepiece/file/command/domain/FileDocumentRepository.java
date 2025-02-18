package depromeet.onepiece.file.command.domain;

import depromeet.onepiece.file.domain.FileDocument;

public interface FileDocumentRepository {
  FileDocument save(FileDocument fileDocument);
}
