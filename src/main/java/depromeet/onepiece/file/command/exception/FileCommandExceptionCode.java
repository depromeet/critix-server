package depromeet.onepiece.file.command.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import depromeet.onepiece.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileCommandExceptionCode implements ErrorCode {
  FILE_CONVERT_ERROR(BAD_REQUEST, "FILE_100", "파일 변환에 실패하였습니다."),
  FILE_UPLOAD_FAILED(BAD_REQUEST, "FILE_200", "파일 업로드에 실패하였습니다."),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}
