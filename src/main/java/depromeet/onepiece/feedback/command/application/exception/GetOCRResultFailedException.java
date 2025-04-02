package depromeet.onepiece.feedback.command.application.exception;

import depromeet.onepiece.common.error.GlobalException;

public class GetOCRResultFailedException extends GlobalException {
  public GetOCRResultFailedException() {
    super(
        FeedbackCommandExceptionCode.GET_OCR_RESULT_FAILED.getMessage(),
        FeedbackCommandExceptionCode.GET_OCR_RESULT_FAILED);
  }
}
