package depromeet.onepiece.feedback.command.application.exception;

import static depromeet.onepiece.feedback.command.application.exception.FeedbackCommandExceptionCode.FEEDBACK_CHATGPT_ERROR;

import depromeet.onepiece.common.error.GlobalException;

public class FeedbackChatGPTErrorException extends GlobalException {
  public FeedbackChatGPTErrorException() {
    super(FEEDBACK_CHATGPT_ERROR.getMessage(), FEEDBACK_CHATGPT_ERROR);
  }
}
