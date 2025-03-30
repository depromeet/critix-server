package depromeet.onepiece.feedback.query.infrastructure;

import depromeet.onepiece.feedback.domain.Feedback;
import java.util.List;
import org.bson.types.ObjectId;

public interface FeedbackQueryRepository {
  Feedback findById(ObjectId feedbackId);

  List<Feedback> findByUserId(ObjectId userId);

  List<Feedback> findRecentFeedback(ObjectId userId);
}
