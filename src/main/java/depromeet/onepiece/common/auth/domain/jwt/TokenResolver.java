package depromeet.onepiece.common.auth.domain.jwt;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface TokenResolver {

  Optional<String> resolveTokenFromRequest(HttpServletRequest request);

  Optional<String> resolveRefreshTokenFromRequest(HttpServletRequest request);

  String getSubjectFromToken(String token);
}
