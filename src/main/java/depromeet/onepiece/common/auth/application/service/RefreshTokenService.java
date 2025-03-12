package depromeet.onepiece.common.auth.application.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenProvider tokenProvider;
  private final TokenResolver tokenResolver;
  private final TokenInjector tokenInjector;

  @Transactional
  public String reissueBasedOnRefreshToken(
      HttpServletRequest request, HttpServletResponse response) {
    RefreshToken refreshTokenDocument =
        getByTokenString(
            tokenResolver
                .resolveRefreshTokenFromRequest(request)
                .orElseThrow(RefreshTokenInvalidException::new));
    tokenInjector.injectRefreshTokenToCookie(
        new TokenResult(rotate(refreshTokenDocument), refreshTokenDocument.getExternalId()),
        response);
    return refreshTokenDocument.getExternalId();
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
