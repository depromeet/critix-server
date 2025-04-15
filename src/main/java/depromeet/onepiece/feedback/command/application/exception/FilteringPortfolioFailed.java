package depromeet.onepiece.feedback.command.application.exception;

import depromeet.onepiece.common.error.GlobalException;

public class FilteringPortfolioFailed extends GlobalException {
  public FilteringPortfolioFailed() {
    super(
        FeedbackCommandExceptionCode.FILTERING_PORTFOLIO_FAILED.getMessage(),
        FeedbackCommandExceptionCode.FILTERING_PORTFOLIO_FAILED);
  }
}
