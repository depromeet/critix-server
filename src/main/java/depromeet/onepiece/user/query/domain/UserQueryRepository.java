package depromeet.onepiece.user.query.domain;

import depromeet.onepiece.user.domain.User;
import java.util.Optional;

public interface UserQueryRepository {

  Optional<User> findByEmail(String email);

  Optional<User> findUserByExternalId(String externalId);
}
