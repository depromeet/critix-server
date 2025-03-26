package depromeet.onepiece.feedback.command.application;

import depromeet.onepiece.feedback.command.infrastructure.FeedbackCommandRepository;
import depromeet.onepiece.feedback.domain.Feedback;
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

  public ObjectId saveEmpty(ObjectId userId, ObjectId fileId) {
    Feedback feedback = Feedback.saveEmptyFeedback(userId, fileId);
    return feedbackCommandRepository.save(feedback).getId();
  }
}
