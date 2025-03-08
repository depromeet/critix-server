package depromeet.onepiece.feedback.query.application;

import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.query.domain.FeedbackQueryRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * 도메인 주도 개발 아키텍처에서 application 계층은 도메인 규칙에 핵심 로직 수행을 위임해요. 따라서 QueryService는 조회한 데이터를 DTO로 변환하거나,
 * 여러 도메인 객체를 조합하여 응답 데이터를 만드는 역할을 합니다.
 */
@Service
@RequiredArgsConstructor
public class FeedbackQueryService {
  private final FeedbackQueryRepository feedbackQueryRepository;

  public Feedback getFeedbackResponse(ObjectId feedbackId) {

    return feedbackQueryRepository.findById(feedbackId);
  }
}
