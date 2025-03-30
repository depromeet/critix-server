package depromeet.onepiece.feedback.query.infrastructure;

import depromeet.onepiece.feedback.domain.ChatGPTConstants;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatGPTConstantsRepository extends MongoRepository<ChatGPTConstants, ObjectId> {
  Optional<ChatGPTConstants> findTopByOrderByCreatedAtDesc();
}
