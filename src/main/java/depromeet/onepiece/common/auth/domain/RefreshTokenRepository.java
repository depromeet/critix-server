package depromeet.onepiece.common.auth.domain;

import java.util.Optional;

public interface RefreshTokenRepository {

  Optional<RefreshToken> findByToken(String token);

  void save(RefreshToken refreshToken);

  Optional<RefreshToken> findByExternalId(String externalId);
}
