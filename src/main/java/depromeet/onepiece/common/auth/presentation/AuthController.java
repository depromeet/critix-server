package depromeet.onepiece.common.auth.presentation;

import depromeet.onepiece.common.auth.application.service.AuthService;
import depromeet.onepiece.common.auth.presentation.response.AccessTokenResponse;
import depromeet.onepiece.common.error.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증/인가 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/reissue")
  @Operation(summary = "액세스 토큰 재발급 API", description = "액세스 토큰 재발급 API [담당자 : 이한음]")
  public CustomResponse<AccessTokenResponse> reissueAccessToken(
      HttpServletRequest request, HttpServletResponse response) {
    return new CustomResponse<>(authService.reissueToken(request, response));
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃 API", description = "로그아웃 API [담당자 : 이한음]")
  public CustomResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    authService.logout(request, response);
    return CustomResponse.ok();
  }
}
