package depromeet.onepiece.feedback.query.application;

import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.query.infrastructure.FeedbackQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackQueryService {
  private final FeedbackQueryRepository feedbackQueryRepository;

  public List<Feedback> findByUserId(ObjectId id) {
    return feedbackQueryRepository.findByUserId(id);
  }
}
