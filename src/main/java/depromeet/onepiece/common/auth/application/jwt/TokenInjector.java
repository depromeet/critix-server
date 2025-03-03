package depromeet.onepiece.common.auth.application.jwt;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

import depromeet.onepiece.common.auth.domain.jwt.LoginResult;
import depromeet.onepiece.common.auth.infrastructure.SecurityProperties;
import depromeet.onepiece.common.auth.infrastructure.jwt.TokenProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenInjector {

  private final TokenProperties tokenProperties;
  private final SecurityProperties securityProperties;

  public void injectTokensToCookie(LoginResult result, HttpServletResponse response) {
    int accessTokenMaxAge = (int) tokenProperties.expirationTime().accessToken() + 5;
    int refreshTokenMaxAge = (int) tokenProperties.expirationTime().refreshToken() + 5;

    addCookie(ACCESS_TOKEN, result.accessToken(), accessTokenMaxAge, response);
    addCookie(REFRESH_TOKEN, result.refreshToken(), refreshTokenMaxAge, response);
  }

  public void addCookie(String name, String value, int maxAge, HttpServletResponse response) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setMaxAge(maxAge);
    cookie.setHttpOnly(securityProperties.cookie().httpOnly());
    cookie.setDomain(securityProperties.cookie().domain());
    cookie.setSecure(securityProperties.cookie().secure());
    cookie.setAttribute("SameSite", "None");

    response.addCookie(cookie);
  }

  public void invalidateCookie(String name, HttpServletResponse response) {
    Cookie cookie = new Cookie(name, null);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    cookie.setHttpOnly(securityProperties.cookie().httpOnly());
    cookie.setDomain(securityProperties.cookie().domain());
    cookie.setSecure(securityProperties.cookie().secure());
    cookie.setAttribute("SameSite", "None");

    response.addCookie(cookie);
  }
}
