package depromeet.onepiece.common.auth.application.handler;

import depromeet.onepiece.common.auth.application.jwt.TokenInjector;
import depromeet.onepiece.common.auth.application.jwt.TokenResult;
import depromeet.onepiece.common.auth.application.service.AuthService;
import depromeet.onepiece.common.auth.domain.CustomOAuth2User;
import depromeet.onepiece.common.auth.infrastructure.SecurityProperties;
import depromeet.onepiece.common.auth.presentation.exception.AlreadyRegisteredUserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final AuthService authService;
  private final TokenInjector tokenInjector;
  private final SecurityProperties securityProperties;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    try {
      TokenResult result = resolveLoginResultFromAuthentication(authentication);
      tokenInjector.injectRefreshTokenToCookie(result, response);
      response.sendRedirect(securityProperties.redirectUrl());
    } catch (AlreadyRegisteredUserException e) {
      throw new AlreadyRegisteredUserException();
    }
  }

  private TokenResult resolveLoginResultFromAuthentication(Authentication authentication) {
    CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    return authService.handleLoginSuccess(oAuth2User.getAuthAttributes());
  }
}
