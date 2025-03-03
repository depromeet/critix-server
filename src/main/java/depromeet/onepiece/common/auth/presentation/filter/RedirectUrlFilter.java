package depromeet.onepiece.common.auth.presentation.filter;

import depromeet.onepiece.common.auth.application.jwt.TokenInjector;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class RedirectUrlFilter extends OncePerRequestFilter {

  private final TokenInjector tokenInjector;

  public static final String REDIRECT_URL_QUERY_PARAM = "redirectUrl";
  public static final String REDIRECT_URL_COOKIE_NAME = "redirect_url";
  private static final List<String> REDIRECT_URL_INJECTION_PATTERNS =
      List.of("/oauth2/authorization/.*");

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (isRedirectRequest(request)) {
      tokenInjector.invalidateCookie(REDIRECT_URL_COOKIE_NAME, response);
      String redirectUri = request.getParameter(REDIRECT_URL_QUERY_PARAM);

      if (StringUtils.hasText(redirectUri)) {
        tokenInjector.addCookie(REDIRECT_URL_COOKIE_NAME, redirectUri, 3600, response);
      }
    }

    filterChain.doFilter(request, response);
  }

  private boolean isRedirectRequest(HttpServletRequest request) {
    return REDIRECT_URL_INJECTION_PATTERNS.stream().anyMatch(request.getRequestURI()::matches);
  }
}
