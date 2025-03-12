package depromeet.onepiece.common.auth.presentation.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import depromeet.onepiece.common.auth.infrastructure.jwt.TokenProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record AccessTokenResponse(
    @Schema(
            description = "액세스 토큰",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyfQ",
            requiredMode = REQUIRED)
        String accessToken,
    @Schema(description = "액세스 토큰 만료 시간", example = "3600", requiredMode = REQUIRED)
        String expirationTime) {
  public static AccessTokenResponse of(String accessToken, TokenProperties tokenProperties) {
    return AccessTokenResponse.builder()
        .accessToken(accessToken)
        .expirationTime(String.valueOf(tokenProperties.expirationTime().accessToken()))
        .build();
  }
}
