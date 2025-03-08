package depromeet.onepiece.common.auth.application.service;

import depromeet.onepiece.common.auth.application.jwt.TokenInjector;
import depromeet.onepiece.common.auth.application.jwt.TokenResult;
import depromeet.onepiece.common.auth.domain.RefreshToken;
import depromeet.onepiece.common.auth.domain.RefreshTokenRepository;
import depromeet.onepiece.common.auth.infrastructure.jwt.JwtTokenProvider;
import depromeet.onepiece.common.auth.infrastructure.jwt.JwtTokenResolver;
import depromeet.onepiece.common.auth.infrastructure.jwt.TokenProperties;
import depromeet.onepiece.common.auth.presentation.exception.AuthenticationRequiredException;
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
  private final JwtTokenProvider tokenProvider;
  private final JwtTokenResolver tokenResolver;
  private final TokenInjector tokenInjector;
  private final TokenProperties tokenProperties;

  @Transactional
  public TokenResult reissueBasedOnRefreshToken(
      HttpServletRequest request, HttpServletResponse response) {
    String refreshToken =
        tokenResolver
            .resolveRefreshTokenFromRequest(request)
            .orElseThrow(AuthenticationRequiredException::new);

    RefreshToken savedRefreshToken = validateAndGetSavedRefreshToken(refreshToken);

    return getReissuedTokenResult(response, savedRefreshToken);
  }

  private TokenResult getReissuedTokenResult(
      HttpServletResponse response, RefreshToken savedRefreshToken) {
    String externalId = savedRefreshToken.getExternalId();
    String rotatedRefreshToken = this.rotate(savedRefreshToken);

    TokenResult tokenResult = new TokenResult(rotatedRefreshToken, false, externalId);
    tokenInjector.injectRefreshTokenToCookie(tokenResult, response);

    return tokenResult;
  }

  private RefreshToken validateAndGetSavedRefreshToken(String refreshToken) {
    String externalId = tokenResolver.getSubjectFromToken(refreshToken);
    RefreshToken savedRefreshToken = this.getByTokenString(refreshToken);

    savedRefreshToken.validateWith(refreshToken, externalId);

    return savedRefreshToken;
  }

  public RefreshToken getByTokenString(String token) {
    return refreshTokenRepository
        .findByToken(token)
        .orElseThrow(AuthenticationRequiredException::new);
  }

  private String rotate(RefreshToken refreshToken) {
    String reissuedToken = tokenProvider.generateRefreshToken(refreshToken.getExternalId());
    refreshToken.rotate(reissuedToken);
    refreshToken.updateExpirationIfExpired(tokenProperties.expirationTime().refreshToken());
    refreshTokenRepository.save(refreshToken);
    return reissuedToken;
  }
}
