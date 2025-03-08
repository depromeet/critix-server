package depromeet.onepiece.common.auth.presentation.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;

@Builder
public record AccessTokenResponse(
    @Schema(
            description = "액세스 토큰",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyfQ",
            requiredMode = REQUIRED)
        String accessToken,
    @Schema(description = "액세스 토큰 만료 시간", example = "3600", requiredMode = REQUIRED)
        String expirationTime,
    @Schema(description = "최초 로그인 여부", example = "false", requiredMode = REQUIRED)
        boolean isFirstLogin) {
  public static AccessTokenResponse of(
      String accessToken, @Value("spring.security.") String expirationTime, boolean isFirstLogin) {
    return AccessTokenResponse.builder()
        .accessToken(accessToken)
        .expirationTime(expirationTime)
        .isFirstLogin(isFirstLogin)
        .build();
  }
}
