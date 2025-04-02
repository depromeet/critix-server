package depromeet.onepiece.feedback.command.infrastructure;

import com.mongodb.client.result.UpdateResult;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.FeedbackStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedbackCommandRepositoryImpl implements FeedbackCommandRepository {
  private final FeedbackCommandMongoRepository feedbackCommandMongoRepository;
  private final MongoTemplate mongoTemplate;

  @Override
  public Feedback save(Feedback feedback) {
    return feedbackCommandMongoRepository.save(feedback);
  }

  @Override
  public Optional<Feedback> findById(ObjectId id) {
    return feedbackCommandMongoRepository.findById(id);
  }

  @Override
  public boolean updateStatus(ObjectId feedbackId, FeedbackStatus status, String fieldName) {
    Query query = new Query(Criteria.where("_id").is(feedbackId));
    Update update = new Update();
    update.set(fieldName, status);
    UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Feedback.class);
    return updateResult.getModifiedCount() == 1;
  }
}
