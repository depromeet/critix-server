package depromeet.onepiece.user.query.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import depromeet.onepiece.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserCommandExceptionCode implements ErrorCode {
  USER_NOT_FOUND(NOT_FOUND, "USER_100", "사용자를 찾을 수 없습니다."),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}
