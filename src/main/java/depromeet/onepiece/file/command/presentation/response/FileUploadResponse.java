package depromeet.onepiece.file.command.presentation.response;

import static depromeet.onepiece.common.utils.EncryptionUtil.decrypt;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import depromeet.onepiece.file.domain.FileDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record FileUploadResponse(
    @Schema(description = "첨부 파일 ID", example = "67b4a9e5da45b044sd3ac", requiredMode = REQUIRED)
        String id,
    @Schema(description = "파일 이름", example = "파일명_예시", requiredMode = REQUIRED) String logicalName,
    @Schema(
            description = "첨부파일 presigned url",
            example = "https://kr.object.ncloudstorage.com/",
            requiredMode = REQUIRED)
        String url) {
  public static FileUploadResponse of(String endpoint, String bucket, FileDocument fileDocument) {
    return FileUploadResponse.builder()
        .id(fileDocument.getId().toString())
        .logicalName(decrypt(fileDocument.getLogicalName()))
        .url(endpoint + "/" + bucket + "/" + decrypt(fileDocument.getPhysicalPath()))
        .build();
  }
}
