package depromeet.onepiece.user.domain;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
  Optional<User> findById(String objectId);
}
