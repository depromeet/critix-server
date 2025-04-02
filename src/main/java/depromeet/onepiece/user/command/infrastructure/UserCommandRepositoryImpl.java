package depromeet.onepiece.user.command.infrastructure;

import depromeet.onepiece.user.command.domain.UserCommandRepository;
import depromeet.onepiece.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCommandRepositoryImpl implements UserCommandRepository {
  private final UserCommandMongoRepository userCommandMongoRepository;

  @Override
  public User save(User user) {
    return userCommandMongoRepository.save(user);
  }
}
