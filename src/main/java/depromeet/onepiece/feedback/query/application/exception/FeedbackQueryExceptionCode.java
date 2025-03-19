package depromeet.onepiece.feedback.query.application.exception;

import depromeet.onepiece.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FeedbackQueryExceptionCode implements ErrorCode {
  FEEDBACK_NOT_FOUND(HttpStatus.NOT_FOUND, "FEEDBACK_NOT_FOUND", "FB001"),
  ;

  private final HttpStatus status;
  private final String message;
  private final String code;
}
