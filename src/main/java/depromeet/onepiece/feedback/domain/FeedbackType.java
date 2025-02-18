package depromeet.onepiece.feedback.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedbackType {
  TRANSLATION_OR_AWKWARD("번역체/어색한 표현"),
  LENGTH_OR_READABILITY("문장이 길거나 가독성이 떨어지는 표현 수정"),
  READABILITY_IMPROVEMENT("가독성 개선"),
  LOGICAL_LEAP("논리적 비약"),
  REDUNDANCY_OR_CLARITY("불필요한 반복 및 의미 명확화");

  private final String description;
}
