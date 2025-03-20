package depromeet.onepiece.feedback.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedbackStatus {
  COMPLETE("COMPLETE"),
  IN_PROGRESS("IN_PROGRESS"),
  ERROR("ERROR");

  private final String description;
}
