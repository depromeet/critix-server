package depromeet.onepiece.user.command.infrastructure;

import depromeet.onepiece.user.domain.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserCommandMongoRepository extends MongoRepository<User, ObjectId> {}
