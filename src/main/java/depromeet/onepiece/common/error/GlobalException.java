package depromeet.onepiece.common.error;

import lombok.Getter;

/** 프로젝트 에러를 반환 할 때 사용하는 기본 클래스 errorCode를 주입받아야 한다. */
public class GlobalException extends RuntimeException {
  @Getter private final GlobalErrorCode errorCode;

  public GlobalException(String message, GlobalErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}
