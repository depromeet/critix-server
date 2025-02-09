package depromeet.onepiece.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum GlobalErrorCode {
	// 공통
	SUCCESS(HttpStatus.OK, "G000", "요청에 성공했습니다."),
	OTHER(HttpStatus.INTERNAL_SERVER_ERROR, "G100", "서버에 오류가 발생했습니다"),

	// 전체
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "G200", "허용되지 않은 메서드입니다"),
	VALID_EXCEPTION(HttpStatus.BAD_REQUEST, "G300", "유효 하지 않은 요청입니다."),
	ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "G400", "허용되지 않은 사용자입니다"),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "G500", "토큰이 만료되었습니다."),
	USER_CONFLICT(HttpStatus.CONFLICT, "G600", "이미 가입된 내역이 있습니다."),

	;
	private final String code;
	private final String message;
	private final HttpStatus status;

	GlobalErrorCode(final HttpStatus status, final String code, final String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
