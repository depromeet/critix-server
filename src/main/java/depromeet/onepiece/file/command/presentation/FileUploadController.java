package depromeet.onepiece.file.command.presentation;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import depromeet.onepiece.file.command.application.FileUploadService;
import depromeet.onepiece.file.command.presentation.response.PresignedUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "FileUpload", description = "파일 업로드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileUploadController {
  private final FileUploadService fileUploadService;

  @Operation(summary = "포트폴리오 첨부 파일 업로드", description = "포트폴리오 첨부 파일 업로드 API [담당자 : 이한음]")
  @PostMapping(value = "/portfolio", consumes = MULTIPART_FORM_DATA_VALUE)
  public PresignedUrlResponse uploadFile(
      @Parameter(
              description = "게시글 첨부 파일",
              content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE),
              required = true)
          @RequestPart(value = "file")
          MultipartFile file) {
    return fileUploadService.uploadPortfolio(file);
  }
}
