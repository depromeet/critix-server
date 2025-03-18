package depromeet.onepiece.feedback.command.infrastructure;

import depromeet.onepiece.feedback.domain.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedbackCommandRepositoryImpl implements FeedbackCommandRepository {
  private final FeedbackCommandMongoRepository feedbackCommandMongoRepository;

  @Override
  public void save(Feedback feedback) {
    feedbackCommandMongoRepository.save(feedback);
  }
}
