package depromeet.onepiece.file.command.application;

import depromeet.onepiece.file.domain.FileType;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
  String upload(MultipartFile file, String originalFileName, FileType fileType);

  default String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex == -1) ? "" : fileName.substring(dotIndex).toLowerCase();
  }

  default String generateUniqueFilePath(String fileExtension) {
    return UUID.randomUUID() + fileExtension;
  }
}
