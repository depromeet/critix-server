package depromeet.onepiece.user.query.domain;

import depromeet.onepiece.user.domain.User;
import java.util.Optional;

public interface UserQueryRepository {

  Optional<User> findUserByEmailAndExternalId(String email, String externalId);

  Optional<User> findUserByExternalId(String externalId);
}
