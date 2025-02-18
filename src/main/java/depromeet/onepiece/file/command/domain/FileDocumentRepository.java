package depromeet.onepiece.file.command.domain;

import depromeet.onepiece.file.domain.FileDocument;

public interface FileDocumentRepository {
  void save(FileDocument fileDocument);
}
