package depromeet.onepiece.common.auth.application.service;

import depromeet.onepiece.common.auth.application.dto.AuthAttributes;
import depromeet.onepiece.common.auth.application.jwt.TokenResult;
import depromeet.onepiece.common.auth.domain.RefreshToken;
import depromeet.onepiece.common.auth.domain.RefreshTokenRepository;
import depromeet.onepiece.common.auth.infrastructure.jwt.TokenProperties;
import depromeet.onepiece.common.auth.infrastructure.jwt.TokenProvider;
import depromeet.onepiece.common.auth.presentation.exception.AlreadyRegisteredUserException;
import depromeet.onepiece.common.auth.presentation.response.AccessTokenResponse;
import depromeet.onepiece.user.command.domain.UserCommandRepository;
import depromeet.onepiece.user.domain.User;
import depromeet.onepiece.user.query.domain.UserQueryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserQueryRepository userQueryRepository;
  private final UserCommandRepository userCommandRepository;
  private final TokenProvider tokenProvider;
  private final TokenProperties tokenProperties;
  private final RefreshTokenService refreshTokenService;
  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional
  public TokenResult handleLoginSuccess(AuthAttributes attributes) {
    return userQueryRepository
        .findUserByEmailAndExternalId(attributes.getEmail(), attributes.getExternalId())
        .map(user -> handleExistUser(user, attributes))
        .orElseGet(() -> handleFirstLogin(attributes));
  }

  public AccessTokenResponse reissueToken(
      HttpServletRequest request, HttpServletResponse response) {
    String reissuedExternalId = refreshTokenService.reissueBasedOnRefreshToken(request, response);
    AccessTokenResponse tokenResult =
        AccessTokenResponse.of(
            tokenProvider.generateAccessToken(reissuedExternalId), tokenProperties);
    log.info(
        "User {} accessed from IP {} and successfully reissued a token",
        maskId(reissuedExternalId),
        request.getRemoteAddr());
    return tokenResult;
  }

  private TokenResult handleExistUser(User user, AuthAttributes attributes) {
    if (user.hasDifferentProviderWithEmail(attributes.getEmail(), attributes.getExternalId()))
      throw new AlreadyRegisteredUserException();
    return generateLoginResult(user);
  }

  private TokenResult handleFirstLogin(AuthAttributes attributes) {
    User newUser = saveNewUser(attributes);
    return generateLoginResult(newUser);
  }

  private TokenResult generateLoginResult(User user) {
    String refreshToken = tokenProvider.generateRefreshToken(user.getExternalId());
    RefreshToken refreshTokenDocument =
        refreshTokenRepository
            .findByExternalId(user.getExternalId())
            .orElse(RefreshToken.of(user.getExternalId(), refreshToken));
    refreshTokenDocument.rotate(refreshToken);
    refreshTokenRepository.save(refreshTokenDocument);
    return new TokenResult(refreshToken, user.getExternalId());
  }

  private User saveNewUser(AuthAttributes attributes) {
    User user = User.save(attributes);
    return userCommandRepository.save(user);
  }

  private String maskId(String externalId) {
    return externalId.substring(0, 4) + "****"; // 앞 4자리만 보이게 마스킹
  }
}
