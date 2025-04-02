package depromeet.onepiece.file.command.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import depromeet.onepiece.file.command.exception.FileConvertErrorException;
import depromeet.onepiece.file.command.exception.FileUploadFailedException;
import depromeet.onepiece.file.command.infrastructure.FileDocumentRepository;
import depromeet.onepiece.file.domain.FileDocument;
import depromeet.onepiece.file.domain.FileType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObjectStorageFileUploader implements FileUploader {

  public static final String PNG = ".png";

  @Value("${cloud.ncp.object-storage.credentials.bucket}")
  private String bucketName;

  private final AmazonS3 amazonS3;
  private final FileDocumentRepository fileRepository;

  @Override
  public FileDocument upload(MultipartFile multipartFile, String logicalName, FileType fileType) {
    try {
      return uploadPdf(multipartFile, logicalName, fileType);
    } catch (Exception e) {
      throw new FileUploadFailedException();
    }
  }

  private void removeUploadedFile(File targetFile) {
    if (targetFile.delete()) {
      log.info("파일 삭제 성공");
    } else {
      log.warn("파일 삭제 실패");
    }
  }

  private Optional<File> convert(MultipartFile file) {
    File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

    try (FileOutputStream fos = new FileOutputStream(convertFile)) {
      fos.write(file.getBytes());
      return Optional.of(convertFile);

    } catch (IOException e) {
      throw new FileConvertErrorException();
    }
  }

  private FileDocument uploadPdf(MultipartFile multipartFile, String logicalName, FileType fileType)
      throws IOException {
    try {
      File pdfFile = convert(multipartFile).orElseThrow(FileConvertErrorException::new);
      List<String> uploadedFilePaths = new ArrayList<>();
      ObjectId fileId = new ObjectId();

      String pdfUploadPath = Path.of(fileId.toString(), "upload", logicalName).toString();
      amazonS3.putObject(
          new PutObjectRequest(bucketName, pdfUploadPath, pdfFile)
              .withCannedAcl(CannedAccessControlList.Private));
      log.info("PDF 업로드: " + pdfUploadPath);

      try (PDDocument document = PDDocument.load(pdfFile)) {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        for (int page = 0; page < document.getNumberOfPages(); page++) {
          BufferedImage image = pdfRenderer.renderImageWithDPI(page, 72);
          String imageName = (page + 1) + PNG;

          try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", bos);
            String processedPath = Path.of(fileId.toString(), "processed", imageName).toString();

            try (ByteArrayInputStream input = new ByteArrayInputStream(bos.toByteArray())) {
              ObjectMetadata metadata = new ObjectMetadata();
              metadata.setContentLength(bos.size());

              amazonS3.putObject(
                  new PutObjectRequest(bucketName, processedPath, input, metadata)
                      .withCannedAcl(CannedAccessControlList.PublicRead));
              log.info("이미지 업로드: " + processedPath);
              uploadedFilePaths.add(processedPath);
            }
          }
        }
      }

      removeUploadedFile(pdfFile);
      uploadCompletedFile(fileId);

      return fileRepository.save(
          FileDocument.create(fileId, logicalName, fileType)
              .setPhysicalPath(pdfUploadPath + "," + String.join(",", uploadedFilePaths)));

    } catch (IOException e) {
      throw new FileUploadFailedException();
    }
  }

  private void uploadCompletedFile(ObjectId fileId) {
    File completedFile = null;
    try {
      completedFile = File.createTempFile("completed-" + fileId, ".txt");
      String completedFilePath =
          Path.of(fileId.toString(), "processed", "completed.txt").toString();
      amazonS3.putObject(
          new PutObjectRequest(bucketName, completedFilePath, completedFile)
              .withCannedAcl(CannedAccessControlList.Private));

      log.info("트리거 파일 업로드: " + completedFilePath);

    } catch (IOException e) {
      log.error("임시 파일 에러", e);
      throw new FileUploadFailedException();
    } finally {
      if (completedFile != null && completedFile.exists()) {
        boolean deleted = completedFile.delete();
        if (!deleted) {
          log.warn("임시 파일 삭제 실패 : " + completedFile.getAbsolutePath());
        }
      }
    }
  }
}
