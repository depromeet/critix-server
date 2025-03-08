package depromeet.onepiece.user.query.exception;

import static depromeet.onepiece.user.query.exception.UserCommandExceptionCode.USER_NOT_FOUND;

import depromeet.onepiece.common.error.GlobalException;

public class UserNotFoundException extends GlobalException {
  public UserNotFoundException() {
    super(USER_NOT_FOUND.getMessage(), USER_NOT_FOUND);
  }
}
