package depromeet.onepiece.feedback.command.presentation.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecentFeedbackListResponse {
  private ObjectId feedbackId;

  private LocalDateTime date;

  private String title;
}
