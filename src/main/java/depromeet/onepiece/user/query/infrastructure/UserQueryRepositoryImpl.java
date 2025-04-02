package depromeet.onepiece.user.query.infrastructure;

import depromeet.onepiece.user.domain.User;
import depromeet.onepiece.user.query.domain.UserQueryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {
  private final UserQueryMongoRepository userQueryMongoRepository;

  @Override
  public Optional<User> findUserByEmailAndExternalId(String email, String externalId) {
    return userQueryMongoRepository.findUserByEmailAndExternalId(email, externalId);
  }

  @Override
  public Optional<User> findUserById(ObjectId id) {
    return userQueryMongoRepository.findById(id);
  }

  @Override
  public Optional<User> findUserByExternalId(String externalId) {
    return userQueryMongoRepository.findByExternalId(externalId);
  }
}
