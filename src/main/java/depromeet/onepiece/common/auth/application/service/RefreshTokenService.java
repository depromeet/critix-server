package depromeet.onepiece.common.auth.application.service;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

import depromeet.onepiece.common.auth.application.jwt.TokenInjector;
import depromeet.onepiece.common.auth.application.jwt.TokenResult;
import depromeet.onepiece.common.auth.domain.RefreshToken;
import depromeet.onepiece.common.auth.domain.RefreshTokenRepository;
import depromeet.onepiece.common.auth.infrastructure.jwt.TokenProvider;
import depromeet.onepiece.common.auth.infrastructure.jwt.TokenResolver;
import depromeet.onepiece.common.auth.presentation.exception.AuthenticationRequiredException;
import depromeet.onepiece.common.auth.presentation.exception.RefreshTokenInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenProvider tokenProvider;
  private final TokenResolver tokenResolver;
  private final TokenInjector tokenInjector;

  @Transactional
  public String reissueBasedOnRefreshToken(
      HttpServletRequest request, HttpServletResponse response) {
    Optional<String> tokenString = tokenResolver.resolveRefreshTokenFromRequest(request);

    isvalidRefreshToken(request, tokenString);

    RefreshToken refreshTokenDocument = getByTokenString(tokenString.get());
    tokenInjector.injectRefreshTokenToCookie(
        new TokenResult(rotate(refreshTokenDocument), refreshTokenDocument.getExternalId()),
        response);
    return refreshTokenDocument.getExternalId();
  }

  public void invalidateRefreshToken(HttpServletRequest request, HttpServletResponse response) {
    Optional<String> tokenString = tokenResolver.resolveRefreshTokenFromRequest(request);
    isvalidRefreshToken(request, tokenString);
    tokenInjector.invalidateCookie(REFRESH_TOKEN, response);
  }

  private static void isvalidRefreshToken(
      HttpServletRequest request, Optional<String> tokenString) {
    if (tokenString.isEmpty()) {
      log.warn(
          "Failed to reissue token: No refresh token found in request from IP {}",
          request.getRemoteAddr());
      throw new RefreshTokenInvalidException();
    }
  }

  public RefreshToken getByTokenString(String token) {
    return refreshTokenRepository
        .findByToken(token)
        .orElseThrow(AuthenticationRequiredException::new);
  }

  private String rotate(RefreshToken refreshToken) {
    String reissuedToken = tokenProvider.generateRefreshToken(refreshToken.getExternalId());
    refreshToken.rotate(reissuedToken);
    refreshTokenRepository.save(refreshToken);
    return reissuedToken;
  }
}
