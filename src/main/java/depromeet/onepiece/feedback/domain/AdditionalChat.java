package depromeet.onepiece.feedback.domain;

import static lombok.AccessLevel.PROTECTED;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class AdditionalChat {

  private boolean mine;

  private String content;
}
