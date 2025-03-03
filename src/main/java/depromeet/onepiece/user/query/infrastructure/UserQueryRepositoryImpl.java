package depromeet.onepiece.user.query.infrastructure;

import depromeet.onepiece.user.domain.User;
import depromeet.onepiece.user.query.domain.UserQueryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {
  private final UserQueryMongoRepository userQueryMongoRepository;

  @Override
  public Optional<User> findByEmail(String email) {
    return userQueryMongoRepository.findByEmail(email);
  }

  @Override
  public Optional<User> findUserByExternalId(String externalId) {
    return Optional.ofNullable(userQueryMongoRepository.findByExternalId(externalId));
  }
}
