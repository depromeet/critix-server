package depromeet.onepiece.common.auth.presentation.exception;

import static depromeet.onepiece.common.auth.presentation.exception.AuthExceptionCode.AUTHENTICATION_REQUIRED;

import depromeet.onepiece.common.error.GlobalException;

public class AuthenticationRequiredException extends GlobalException {
  public AuthenticationRequiredException() {
    super(AUTHENTICATION_REQUIRED.getMessage(), AUTHENTICATION_REQUIRED);
  }
}
