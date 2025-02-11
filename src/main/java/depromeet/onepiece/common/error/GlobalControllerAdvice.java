package depromeet.onepiece.common.error;

import java.nio.file.AccessDeniedException;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

  /** Valid 검증 실패시 오류 발생 주로 @RequestBody, @RequestPart 어노테이션에서 발생 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CustomResponse<Void>> handleValidationException(
      MethodArgumentNotValidException ex) {
    String errorMessage =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

    log.error("Error Message : {}", errorMessage);
    CustomResponse<Void> error =
        CustomResponse.error(GlobalErrorCode.VALID_EXCEPTION, errorMessage);
    return ResponseEntity.status(error.getStatus()).body(error);
  }

  /** 변수 Binding시 발생하는 오류 */
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<CustomResponse<Void>> invalidArgumentBindResponse(BindException exception) {
    log.error(
        "Exception : {}, 입력값 : {}",
        exception.getBindingResult().getFieldError(),
        exception.getBindingResult().getFieldError());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            CustomResponse.error(
                GlobalErrorCode.VALID_EXCEPTION,
                Objects.requireNonNull(exception.getBindingResult().getFieldError())
                    .getDefaultMessage()));
  }

  /** 지원하지 않은 HTTP method 호출 할 경우 발생 */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  protected ResponseEntity<CustomResponse<Void>> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException exception) {
    log.error("handleHttpRequestMethodNotSupportedException", exception);

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(CustomResponse.error(GlobalErrorCode.METHOD_NOT_ALLOWED));
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  protected ResponseEntity<CustomResponse<Void>> handleAccessDeniedException(
      AccessDeniedException exception) {
    log.info("{}", exception.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(CustomResponse.error(GlobalErrorCode.ACCESS_DENIED));
  }

  /**
   * 프로젝트내 설정한 예외가 발생할때 처리하는 부분
   *
   * @param exception 발생한 예외
   * @return 예외를 처리해서 반환한다.
   */
  @ExceptionHandler(GlobalException.class)
  protected ResponseEntity<CustomResponse<Void>> handleGlobalBaseException(
      final GlobalException exception) {
    log.error(
        "{} Exception {}: {}",
        exception.getErrorCode(),
        exception.getErrorCode().getCode(),
        exception.getErrorCode().getMessage());

    return ResponseEntity.status(exception.getErrorCode().getStatus())
        .body(CustomResponse.error(exception.getErrorCode(), exception.getMessage()));
  }

  /**
   * 처리되지 않은 에러를 여기서 처리 한다.
   *
   * @param exception 발생한 에러
   * @return CustomResponse로 메시지를 감춰서 반환한다.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected ResponseEntity<CustomResponse<Void>> handleException(Exception exception) {
    log.error("Exception : {}", GlobalErrorCode.OTHER.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(CustomResponse.error(GlobalErrorCode.OTHER));
  }
}
