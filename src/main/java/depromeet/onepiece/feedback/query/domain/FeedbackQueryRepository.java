package depromeet.onepiece.feedback.query.domain;

import depromeet.onepiece.feedback.domain.Feedback;
import java.util.List;
import org.bson.types.ObjectId;

public interface FeedbackQueryRepository {
  Feedback findById(ObjectId feedbackId);

  List<Feedback> findByUserId(String userId);
}
