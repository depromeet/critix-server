package depromeet.onepiece.user.query.exception;

import static depromeet.onepiece.user.query.exception.UserCommandExceptionCode.USER_NOT_AUTHENTICATED;

import depromeet.onepiece.common.error.GlobalException;

public class UserNotAuthenticatedException extends GlobalException {
  public UserNotAuthenticatedException() {
    super(USER_NOT_AUTHENTICATED.getMessage(), USER_NOT_AUTHENTICATED);
  }
}
