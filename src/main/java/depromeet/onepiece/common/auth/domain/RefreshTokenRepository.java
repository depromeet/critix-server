package depromeet.onepiece.common.auth.domain;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findByExternalId(String externalId);
}
