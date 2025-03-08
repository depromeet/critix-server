package depromeet.onepiece.common.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Objects;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
// @JsonPropertyOrder : json serialization 순서를 정의
@JsonPropertyOrder({"status", "code", "message", "result"})
public class CustomResponse<T> {
  private final HttpStatus status;
  private final String message;
  private final String code;

  @JsonInclude(JsonInclude.Include.NON_NULL) // 결과값이 공백일 경우 json에 포함하지 않도록
  private T result;

  @Override
  public String toString() {
    return "CustomResponse{"
        + "status="
        + status
        + ", message='"
        + message
        + '\''
        + ", code='"
        + code
        + '\''
        + ", result="
        + result
        + '}';
  }

  public static <T> ResponseEntity<CustomResponse<T>> okResponseEntity(T result) {
    return ResponseEntity.ok(new CustomResponse<>(result));
  }

  public static <T> ResponseEntity<CustomResponse<T>> okResponseEntity() {
    return ResponseEntity.ok(new CustomResponse<>());
  }

  public static CustomResponse<Void> error(ErrorCode errorCode) {
    return new CustomResponse<>(errorCode);
  }

  public static CustomResponse<Void> error(ErrorCode errorCode, String message) {
    return new CustomResponse<>(errorCode, message);
  }

  // 요청에 성공한 경우
  private CustomResponse() {
    this.status = GlobalErrorCode.SUCCESS.getStatus();
    this.message = GlobalErrorCode.SUCCESS.getMessage();
    this.code = GlobalErrorCode.SUCCESS.getCode();
  }

  public CustomResponse(T result) {
    Objects.requireNonNull(result, "Result must not be null");
    this.status = GlobalErrorCode.SUCCESS.getStatus();
    this.message = GlobalErrorCode.SUCCESS.getMessage();
    this.code = GlobalErrorCode.SUCCESS.getCode();
    this.result = result;
  }

  // 요청에 실패한 경우
  private CustomResponse(ErrorCode errorCode) {
    Objects.requireNonNull(errorCode, "Global error code must not be null");
    this.status = errorCode.getStatus();
    this.message = errorCode.getMessage();
    this.code = errorCode.getCode();
  }

  // GlobalControllerAdvice에서 오류 설정
  private CustomResponse(ErrorCode errorCode, String message) {
    Objects.requireNonNull(errorCode, "Result must not be null");
    Objects.requireNonNull(message, "Message must not be null");

    this.status = errorCode.getStatus();
    this.message = message;
    this.code = errorCode.getCode();
  }
}
