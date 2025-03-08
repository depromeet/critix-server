package depromeet.onepiece.common.auth.infrastructure;

import depromeet.onepiece.common.auth.domain.RefreshToken;
import depromeet.onepiece.common.auth.domain.RefreshTokenRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

  private final RefreshTokenMongoRepository refreshTokenMongoRepository;

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenMongoRepository.findByToken(token);
  }

  @Override
  public Optional<RefreshToken> findByExternalId(String externalId) {
    return refreshTokenMongoRepository.findFirstByExternalId(externalId);
  }

  @Override
  public void save(RefreshToken refreshToken) {
    refreshTokenMongoRepository.save(refreshToken);
  }
}
