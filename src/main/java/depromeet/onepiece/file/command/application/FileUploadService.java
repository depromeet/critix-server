package depromeet.onepiece.file.command.application;

import depromeet.onepiece.file.command.infrastructure.PresignedUrlProperties;
import depromeet.onepiece.file.command.presentation.response.PresignedUrlResponse;
import depromeet.onepiece.file.domain.FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {
  private final FileUploader fileUploader;
  private final PresignedUrlProperties presignedUrlProperties;

  public PresignedUrlResponse uploadPortfolio(MultipartFile multipartFile) {
    return PresignedUrlResponse.of(
        presignedUrlProperties.endpoint(),
        presignedUrlProperties.bucket(),
        fileUploader.upload(
            multipartFile, multipartFile.getOriginalFilename(), FileType.PORTFOLIO_PDF));
  }
}
