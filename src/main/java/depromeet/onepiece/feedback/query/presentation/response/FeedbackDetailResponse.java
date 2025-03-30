package depromeet.onepiece.feedback.query.presentation.response;

import depromeet.onepiece.feedback.domain.Feedback;
import java.util.List;

public record FeedbackDetailResponse(Feedback feedback, List<String> imageList) {}
