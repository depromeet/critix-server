package depromeet.onepiece.file.command.application;

import depromeet.onepiece.file.command.domain.FileDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUploadService {
  private final FileDocumentRepository fileDocumentRepository;
}
