package depromeet.onepiece.feedback.command.application.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import depromeet.onepiece.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FeedbackCommandExceptionCode implements ErrorCode {
  FEEDBACK_CHATGPT_ERROR(BAD_REQUEST, "FEEDBACK_100", "ChatGPT 요청에 실패하였습니다."),
  OCR_NOT_FOUND(BAD_REQUEST, "FEEDBACK_101", "OCR 결과를 찾을 수 없습니다."),
  GET_OCR_RESULT_FAILED(BAD_REQUEST, "FEEDBACK_102", "OCR 결과를 가져오는데 실패하였습니다."),
  FILTERING_PORTFOLIO_FAILED(BAD_REQUEST, "FEEDBACK_103", "포트폴리오 필터링에 실패하였습니다."),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;
}
