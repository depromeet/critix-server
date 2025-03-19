package depromeet.onepiece.feedback.command.infrastructure;

import depromeet.onepiece.feedback.domain.Feedback;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackCommandMongoRepository extends MongoRepository<Feedback, ObjectId> {}
