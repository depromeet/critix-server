package depromeet.onepiece.file.command.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import depromeet.onepiece.file.command.application.FileUploader;
import depromeet.onepiece.file.command.domain.FileDocumentRepository;
import depromeet.onepiece.file.command.exception.FileConvertErrorException;
import depromeet.onepiece.file.command.exception.FileUploadFailedException;
import depromeet.onepiece.file.domain.FileDocument;
import depromeet.onepiece.file.domain.FileType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploaderImpl implements FileUploader {

  @Value("${cloud.ncp.object-storage.credentials.bucket}")
  private String bucketName;

  private final AmazonS3 amazonS3;
  private final FileDocumentRepository fileRepository;

  @Override
  public String upload(MultipartFile multipartFile, String logicalName, FileType fileType) {
    try {
      File fileToUpload = convert(multipartFile).orElseThrow(FileConvertErrorException::new);
      FileDocument fileDocumentToSave = FileDocument.create(logicalName, fileType);
      String filePath =
          generateUploadPath(fileDocumentToSave.getId().toString(), logicalName, fileType);
      amazonS3.putObject(
          new PutObjectRequest(bucketName, filePath, fileToUpload)
              .withCannedAcl(CannedAccessControlList.PublicRead));
      removeUploadedFile(fileToUpload);
      fileRepository.save(fileDocumentToSave.setPhysicalPath(filePath));
      return filePath;
    } catch (Exception e) {
      throw new FileUploadFailedException();
    }
  }

  private void removeUploadedFile(File targetFile) {
    if (targetFile.delete()) {
      log.info("File deleted successfully");
    } else {
      log.warn("Failed to delete the file");
    }
  }

  private Optional<File> convert(MultipartFile file) {
    File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
    try {
      if (convertFile.createNewFile()) {
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
          fos.write(file.getBytes());
        }
        return Optional.of(convertFile);
      }
    } catch (IOException e) {
      throw new FileConvertErrorException();
    }
    return Optional.empty();
  }

  private String generateUploadPath(String fileId, String logicalName, FileType fileType) {
    String fileExtension = getFileExtension(logicalName);
    String fileName = generateUniqueFilePath(fileExtension);
    return switch (fileType) {
      case PORTFOLIO_PDF -> Path.of(fileId, "upload", fileName).toString();
      case PORTFOLIO_IMAGE -> Path.of(fileId, "processed", fileName).toString();
    };
  }
}
