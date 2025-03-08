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
import java.awt.image.BufferedImage;
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

  private FileDocument uploadPdf(
      MultipartFile multipartFile, String logicalName, FileType fileType) {
    try {
      File pdfFile = convert(multipartFile).orElseThrow(FileConvertErrorException::new);
      List<String> uploadedFilePaths = new ArrayList<>();
      ObjectId fileId = new ObjectId();

      String pdfUploadPath = Path.of(fileId.toString(), "upload", logicalName).toString();
      amazonS3.putObject(
          new PutObjectRequest(bucketName, pdfUploadPath, pdfFile)
              .withCannedAcl(CannedAccessControlList.Private));
      log.info("PDF uploaded to S3: " + pdfUploadPath);

      try (PDDocument document = PDDocument.load(pdfFile)) {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        for (int page = 0; page < document.getNumberOfPages(); page++) {
          BufferedImage image = pdfRenderer.renderImageWithDPI(page, 30);
          String imageName = (page + 1) + ".png";
          File imageFile = new File(imageName);
          ImageIO.write(image, "png", imageFile);

          String processedPath = Path.of(fileId.toString(), "processed", imageName).toString();
          amazonS3.putObject(
              new PutObjectRequest(bucketName, processedPath, imageFile)
                  .withCannedAcl(CannedAccessControlList.Private));
          log.info("Image uploaded to S3: " + processedPath);

          uploadedFilePaths.add(processedPath);
          removeUploadedFile(imageFile);
        }
      }

      removeUploadedFile(pdfFile);
      return fileRepository.save(
          FileDocument.create(fileId, logicalName, fileType)
              .setPhysicalPath(pdfUploadPath + "," + String.join(",", uploadedFilePaths)));
    } catch (IOException e) {
      throw new FileUploadFailedException();
    }
  }
}
