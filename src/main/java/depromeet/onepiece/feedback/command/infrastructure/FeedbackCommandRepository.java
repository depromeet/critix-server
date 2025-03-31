package depromeet.onepiece.feedback.command.infrastructure;

import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.FeedbackStatus;
import java.util.Optional;
import org.bson.types.ObjectId;

public interface FeedbackCommandRepository {
  Feedback save(Feedback feedback);

  Optional<Feedback> findById(ObjectId feedbackId);

  boolean updateStatus(ObjectId feedbackId, FeedbackStatus status, String fieldName);
}
