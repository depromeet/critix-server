package depromeet.onepiece.feedback.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalChat {

  private boolean mine;

  private String content;
}
