package depromeet.onepiece.feedback.command.infrastructure;

import depromeet.onepiece.feedback.domain.Feedback;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedbackCommandRepositoryImpl implements FeedbackCommandRepository {
  private final FeedbackCommandMongoRepository feedbackCommandMongoRepository;

  @Override
  public Feedback save(Feedback feedback) {
    return feedbackCommandMongoRepository.save(feedback);
  }

  @Override
  public Optional<Feedback> findById(ObjectId id) {
    return feedbackCommandMongoRepository.findById(id);
  }
}
