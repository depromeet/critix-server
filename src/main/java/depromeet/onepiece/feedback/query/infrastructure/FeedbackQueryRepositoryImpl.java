package depromeet.onepiece.feedback.query.infrastructure;

import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.query.application.exception.FeedbackNotFoundException;
import depromeet.onepiece.feedback.query.domain.FeedbackQueryRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedbackQueryRepositoryImpl implements FeedbackQueryRepository {
  private final FeedbackMongoRepository feedbackMongoRepository;

  @Override
  public Feedback findById(ObjectId feedbackId) {
    return feedbackMongoRepository.findById(feedbackId).orElseThrow(FeedbackNotFoundException::new);
  }
}
