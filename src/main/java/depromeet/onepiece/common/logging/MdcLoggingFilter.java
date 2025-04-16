package depromeet.onepiece.common.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MdcLoggingFilter implements Filter {

  private static final String USER_ID_KEY = "userId";
  private static final String REQUEST_ID_KEY = "requestId";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      MDC.put(REQUEST_ID_KEY, UUID.randomUUID().toString());
      String userId = SecurityContextHolder.getContext().getAuthentication().getName();
      MDC.put(USER_ID_KEY, userId);
      chain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
  }
}
