package depromeet.onepiece.feedback.query.infrastructure;

import depromeet.onepiece.feedback.domain.Feedback;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackQueryMongoRepository extends MongoRepository<Feedback, ObjectId> {
  List<Feedback> findByUserId(ObjectId userId);

  List<Feedback> findByUserIdOrderByCreatedAtDesc(ObjectId userId);
}
