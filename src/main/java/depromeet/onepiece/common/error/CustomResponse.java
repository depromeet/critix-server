package depromeet.onepiece.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
// @JsonPropertyOrder : json serialization 순서를 정의
@JsonPropertyOrder({"status", "code", "message", "result"})
public class CustomResponse<T> {
	private HttpStatus status;
	private String message;
	private String code;
	@JsonInclude(JsonInclude.Include.NON_NULL) // 결과값이 공백일 경우 json에 포함하지 않도록
	private T result;

	@Override
	public String toString() {
		return "CustomResponse{" +
			"status=" + status +
			", message='" + message + '\'' +
			", code='" + code + '\'' +
			", result=" + result +
			'}';
	}

	public static <T> ResponseEntity<CustomResponse<T>> okResponseEntity(T result) {
		return ResponseEntity.ok(new CustomResponse<>(result));
	}

	public static <T> ResponseEntity<CustomResponse<T>> okResponseEntity() {
		return ResponseEntity.ok(new CustomResponse<>());
	}

	public static CustomResponse<Void> error(GlobalErrorCode errorCode) {
		return new CustomResponse<>(errorCode);
	}

	public static CustomResponse<Void> error(GlobalErrorCode errorCode, String message) {
		return new CustomResponse<>(errorCode, message);
	}

	// 요청에 성공한 경우
	private CustomResponse() {
		this.status = GlobalErrorCode.SUCCESS.getStatus();
		this.message = GlobalErrorCode.SUCCESS.getMessage();
		this.code = GlobalErrorCode.SUCCESS.getCode();
	}

	private CustomResponse(T result) {
		this.status = GlobalErrorCode.SUCCESS.getStatus();
		this.message = GlobalErrorCode.SUCCESS.getMessage();
		this.code = GlobalErrorCode.SUCCESS.getCode();
		this.result = result;
	}

	private CustomResponse(GlobalErrorCode code, T result) {
		this.status = code.getStatus();
		this.message = code.getMessage();
		this.code = code.getCode();
		this.result = result;
	}

	// 요청에 실패한 경우
	private CustomResponse(GlobalErrorCode errorCode) {
		this.status = errorCode.getStatus();
		this.message = errorCode.getMessage();
		this.code = errorCode.getCode();
	}

	// GlobalControllerAdvice에서 오류 설정
	private CustomResponse(GlobalErrorCode errorCode, String message) {
		this.status = errorCode.getStatus();
		this.message = message;
		this.code = errorCode.getCode();
	}
}
