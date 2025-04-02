package depromeet.onepiece.feedback.query.presentation.response;

import java.time.LocalDate;
import org.bson.types.ObjectId;

public record RecentFeedbackListResponse(ObjectId feedbackId, LocalDate date, String title) {}
