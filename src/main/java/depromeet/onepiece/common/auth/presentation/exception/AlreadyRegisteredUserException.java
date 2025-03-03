package depromeet.onepiece.common.auth.presentation.exception;

import static depromeet.onepiece.common.auth.presentation.exception.AuthExceptionCode.ALREADY_REGISTERED_USER;

import depromeet.onepiece.common.error.GlobalException;

public class AlreadyRegisteredUserException extends GlobalException {

  public AlreadyRegisteredUserException() {
    super(ALREADY_REGISTERED_USER.getMessage(), ALREADY_REGISTERED_USER);
  }
}
