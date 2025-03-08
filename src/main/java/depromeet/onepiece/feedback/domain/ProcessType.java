package depromeet.onepiece.feedback.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProcessType {
  GOOD("good"),
  BAD("bad"),
  SOSO("soso");

  private final String description;
}
