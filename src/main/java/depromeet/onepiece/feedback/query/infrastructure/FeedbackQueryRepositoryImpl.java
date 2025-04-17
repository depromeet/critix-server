package depromeet.onepiece.feedback.query.infrastructure;

import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.query.application.exception.FeedbackNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedbackQueryRepositoryImpl implements FeedbackQueryRepository {
  private final FeedbackQueryMongoRepository feedbackQueryMongoRepository;

  @Override
  public Feedback findById(ObjectId feedbackId) {
    return feedbackQueryMongoRepository
        .findById(feedbackId)
        .orElseThrow(FeedbackNotFoundException::new);
  }

  @Override
  public List<Feedback> findByUserIdOrderByCreatedAtDesc(ObjectId userId) {
    return feedbackQueryMongoRepository.findByUserIdOrderByCreatedAtDesc(userId);
  }

  @Override
  public List<Feedback> findRecentFeedback(ObjectId userId) {

    return feedbackQueryMongoRepository.findByUserIdOrderByCreatedAtDesc(userId);
  }
}
