package depromeet.onepiece.user.command.domain;

import depromeet.onepiece.user.domain.User;

public interface UserCommandRepository {
  User save(User user);
}
