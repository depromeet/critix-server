package depromeet.onepiece.feedback.command.application;

import depromeet.onepiece.feedback.command.infrastructure.FeedbackCommandRepository;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.FeedbackStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackCommandService {
  private final FeedbackCommandRepository feedbackCommandRepository;

  public Feedback save(Feedback feedback) {
    return feedbackCommandRepository.save(feedback);
  }

  public Feedback saveEmpty(ObjectId userId, ObjectId fileId) {
    Feedback feedback =
        new Feedback(
            new ObjectId(),
            userId,
            fileId,
            null,
            FeedbackStatus.PENDING,
            FeedbackStatus.PENDING,
            null,
            null,
            null);

    return feedbackCommandRepository.save(feedback);
  }
}
