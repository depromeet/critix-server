package depromeet.onepiece.file.command.presentation.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;

public record PresignedUrlResponse(
    @Schema(
            description = "첨부파일 presigned url",
            example = "https://example.com",
            requiredMode = REQUIRED)
        String url) {
  public static PresignedUrlResponse of(String url) {
    return new PresignedUrlResponse(url);
  }
}
