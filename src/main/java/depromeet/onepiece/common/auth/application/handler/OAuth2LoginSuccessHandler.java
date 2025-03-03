package depromeet.onepiece.common.auth.application.handler;

import static depromeet.onepiece.common.auth.presentation.exception.AuthExceptionCode.ALREADY_REGISTERED_USER;
import static depromeet.onepiece.common.auth.presentation.filter.RedirectUrlFilter.REDIRECT_URL_COOKIE_NAME;

import depromeet.onepiece.common.auth.application.jwt.TokenInjector;
import depromeet.onepiece.common.auth.application.service.AuthService;
import depromeet.onepiece.common.auth.domain.CustomOAuth2User;
import depromeet.onepiece.common.auth.domain.jwt.LoginResult;
import depromeet.onepiece.common.auth.infrastructure.SecurityProperties;
import depromeet.onepiece.common.auth.presentation.exception.AlreadyRegisteredUserException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final AuthService authService;
  private final TokenInjector tokenInjector;

  private final SecurityProperties securityProperties;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    try {
      LoginResult result = resolveLoginResultFromAuthentication(authentication);
      tokenInjector.injectTokensToCookie(result, response);
      redirectToSuccessUrl(request, response);
    } catch (AlreadyRegisteredUserException e) {
      handleAlreadyExistUser(response);
    }
  }

  private LoginResult resolveLoginResultFromAuthentication(Authentication authentication) {
    CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    return authService.handleLoginSuccess(oAuth2User.getAuthAttributes());
  }

  private void redirectToSuccessUrl(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String redirectUrlByCookie = getRedirectUrlByCookie(request);
    String redirectUrl = determineRedirectUrl(redirectUrlByCookie);
    response.sendRedirect(redirectUrl);
    tokenInjector.invalidateCookie(REDIRECT_URL_COOKIE_NAME, response);
  }

  private String getRedirectUrlByCookie(HttpServletRequest request) {
    return Arrays.stream(request.getCookies())
        .filter(cookie -> Objects.equals(cookie.getName(), REDIRECT_URL_COOKIE_NAME))
        .findFirst()
        .map(Cookie::getValue)
        .orElse(null);
  }

  private String determineRedirectUrl(String redirectCookie) {
    if (StringUtils.hasText(redirectCookie)) {
      return redirectCookie;
    }
    return securityProperties.redirectUrl();
  }

  private void handleAlreadyExistUser(HttpServletResponse response) throws IOException {
    response.sendRedirect(
        securityProperties.loginUrl() + "?error=true&exception=" + ALREADY_REGISTERED_USER);
  }
}
