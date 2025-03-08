package depromeet.onepiece.common.auth.infrastructure;

import depromeet.onepiece.common.auth.domain.RefreshToken;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenMongoRepository extends MongoRepository<RefreshToken, ObjectId> {

  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findFirstByExternalId(String externalId);
}
