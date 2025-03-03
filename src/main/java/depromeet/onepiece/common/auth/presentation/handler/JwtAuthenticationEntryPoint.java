package depromeet.onepiece.common.auth.presentation.handler;

import static depromeet.onepiece.common.auth.presentation.exception.AuthExceptionCode.AUTHENTICATION_REQUIRED;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import depromeet.onepiece.common.error.CustomResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    setResponseBodyBasicInfo(response);
    objectMapper.writeValue(
        response.getOutputStream(), CustomResponse.error(AUTHENTICATION_REQUIRED));
  }

  private void setResponseBodyBasicInfo(HttpServletResponse response) {
    response.setStatus(SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
  }
}
