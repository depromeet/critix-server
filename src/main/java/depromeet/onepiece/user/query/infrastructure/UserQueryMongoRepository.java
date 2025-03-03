package depromeet.onepiece.user.query.infrastructure;

import depromeet.onepiece.user.domain.User;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserQueryMongoRepository extends MongoRepository<User, ObjectId> {
  Optional<User> findByEmail(String email);

  User findByExternalId(String externalId);
}
