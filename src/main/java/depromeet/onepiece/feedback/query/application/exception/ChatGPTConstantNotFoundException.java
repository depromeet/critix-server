package depromeet.onepiece.feedback.query.application.exception;

import depromeet.onepiece.common.error.GlobalException;

public class ChatGPTConstantNotFoundException extends GlobalException {
  public ChatGPTConstantNotFoundException() {
    super(
        FeedbackQueryExceptionCode.CHAT_GPT_CONSTANT_NOT_FOUND.getMessage(),
        FeedbackQueryExceptionCode.CHAT_GPT_CONSTANT_NOT_FOUND);
  }
}
