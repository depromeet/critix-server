package depromeet.onepiece.user.query.domain;

import depromeet.onepiece.user.domain.User;
import java.util.Optional;
import org.bson.types.ObjectId;

public interface UserQueryRepository {
  Optional<User> findUserById(ObjectId id);

  Optional<User> findUserByExternalId(String externalId);

  Optional<User> findUserByEmailAndExternalId(String email, String externalId);
}
