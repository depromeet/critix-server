package depromeet.onepiece.common.auth.application.service;

import depromeet.onepiece.common.auth.application.dto.AuthAttributes;
import depromeet.onepiece.common.auth.domain.RefreshToken;
import depromeet.onepiece.common.auth.domain.RefreshTokenRepository;
import depromeet.onepiece.common.auth.domain.jwt.LoginResult;
import depromeet.onepiece.common.auth.domain.jwt.TokenProvider;
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
  private final TokenProvider tokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenProperties tokenProperties;

  @Transactional
  public LoginResult handleLoginSuccess(AuthAttributes attributes) {
    String email = attributes.getEmail();

    return userQueryRepository
        .findByEmail(email)
        .map(user -> handleExistUser(user, attributes))
        .orElseGet(() -> handleFirstLogin(attributes));
  }

  private LoginResult handleExistUser(User user, AuthAttributes attributes) {
    if (user.hasDifferentProviderWithEmail(attributes.getEmail(), attributes.getExternalId())) {
      throw new AlreadyRegisteredUserException();
    }

    return generateLoginResult(user, false);
  }

  private LoginResult handleFirstLogin(AuthAttributes attributes) {
    User newUser = saveNewUser(attributes);
    return generateLoginResult(newUser, true);
  }

  private LoginResult generateLoginResult(User user, boolean firstLogin) {
    String accessToken = tokenProvider.generateAccessToken(user.getExternalId());
    String refreshToken = tokenProvider.generateRefreshToken(user.getExternalId());

    RefreshToken refreshTokenEntity =
        refreshTokenRepository
            .findByUserId(user.getId())
            .orElse(
                RefreshToken.of(
                    user.getExternalId(),
                    refreshToken,
                    tokenProperties.expirationTime().refreshToken()));

    refreshTokenEntity.rotate(refreshToken);
    refreshTokenRepository.save(refreshTokenEntity);

    return new LoginResult(accessToken, refreshToken, firstLogin, user.getExternalId());
  }

  private User saveNewUser(AuthAttributes attributes) {
    User user = User.save(attributes);
    return userCommandRepository.save(user);
  }
}
