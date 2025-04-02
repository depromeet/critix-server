package depromeet.onepiece.common.auth.presentation.exception;

import static depromeet.onepiece.common.auth.presentation.exception.AuthExceptionCode.REFRESH_TOKEN_INVALID;

import depromeet.onepiece.common.error.GlobalException;

public class RefreshTokenInvalidException extends GlobalException {
  public RefreshTokenInvalidException() {
    super(REFRESH_TOKEN_INVALID.getMessage(), REFRESH_TOKEN_INVALID);
  }
}
