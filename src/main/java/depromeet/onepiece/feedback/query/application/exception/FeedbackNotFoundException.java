package depromeet.onepiece.feedback.query.application.exception;

import depromeet.onepiece.common.error.GlobalException;

public class FeedbackNotFoundException extends GlobalException {
  public FeedbackNotFoundException() {
    super(
        FeedbackQueryExceptionCode.FEEDBACK_NOT_FOUND.getMessage(),
        FeedbackQueryExceptionCode.FEEDBACK_NOT_FOUND);
  }
}
