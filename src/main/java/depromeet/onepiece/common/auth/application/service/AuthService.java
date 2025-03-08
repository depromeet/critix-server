package depromeet.onepiece.common.auth.application.service;

import depromeet.onepiece.common.auth.application.dto.AuthAttributes;
import depromeet.onepiece.common.auth.application.jwt.TokenResult;
import depromeet.onepiece.common.auth.domain.RefreshToken;
import depromeet.onepiece.common.auth.domain.RefreshTokenRepository;
import depromeet.onepiece.common.auth.infrastructure.jwt.JwtTokenProvider;
import depromeet.onepiece.common.auth.infrastructure.jwt.TokenProperties;
import depromeet.onepiece.common.auth.presentation.exception.AlreadyRegisteredUserException;
import depromeet.onepiece.user.command.domain.UserCommandRepository;
import depromeet.onepiece.user.domain.User;
import depromeet.onepiece.user.query.domain.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserQueryRepository userQueryRepository;
  private final UserCommandRepository userCommandRepository;
  private final JwtTokenProvider tokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenProperties tokenProperties;

  @Transactional
  public TokenResult handleLoginSuccess(AuthAttributes attributes) {
    String email = attributes.getEmail();
    return userQueryRepository
        .findByEmail(email)
        .map(user -> handleExistUser(user, attributes))
        .orElseGet(() -> handleFirstLogin(attributes));
  }

  private TokenResult handleExistUser(User user, AuthAttributes attributes) {
    if (user.hasDifferentProviderWithEmail(attributes.getEmail(), attributes.getExternalId()))
      throw new AlreadyRegisteredUserException();
    return generateLoginResult(user, false);
  }

  private TokenResult handleFirstLogin(AuthAttributes attributes) {
    User newUser = saveNewUser(attributes);
    return generateLoginResult(newUser, true);
  }

  private TokenResult generateLoginResult(User user, boolean isFirstLogin) {
    String refreshToken = tokenProvider.generateRefreshToken(user.getExternalId());
    RefreshToken refreshTokenDocument =
        refreshTokenRepository
            .findByExternalId(user.getExternalId())
            .orElse(
                RefreshToken.of(
                    user.getExternalId(),
                    refreshToken,
                    tokenProperties.expirationTime().refreshToken()));
    refreshTokenDocument.rotate(refreshToken);
    refreshTokenRepository.save(refreshTokenDocument);
    return new TokenResult(refreshToken, isFirstLogin, user.getExternalId());
  }

  private User saveNewUser(AuthAttributes attributes) {
    User user = User.save(attributes);
    return userCommandRepository.save(user);
  }
}
