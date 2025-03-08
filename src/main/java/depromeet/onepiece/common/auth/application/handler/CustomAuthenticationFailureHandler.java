package depromeet.onepiece.common.auth.application.handler;

import static depromeet.onepiece.common.auth.presentation.exception.AuthExceptionCode.AUTHENTICATION_REQUIRED;

import depromeet.onepiece.common.auth.infrastructure.SecurityProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  private final SecurityProperties securityProperties;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException, ServletException {
    super.setDefaultFailureUrl(
        securityProperties.loginUrl() + "?error=true&exception=" + AUTHENTICATION_REQUIRED);
    super.onAuthenticationFailure(request, response, exception);
  }
}
