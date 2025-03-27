package depromeet.onepiece.feedback.command.application.exception;

import depromeet.onepiece.common.error.GlobalException;

public class OCRResultNotFoundException extends GlobalException {
  public OCRResultNotFoundException() {
    super(
        FeedbackCommandExceptionCode.OCR_NOT_FOUND.getMessage(),
        FeedbackCommandExceptionCode.OCR_NOT_FOUND);
  }
}
