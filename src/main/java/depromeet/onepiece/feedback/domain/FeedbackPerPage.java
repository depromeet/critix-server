package depromeet.onepiece.feedback.domain;

import org.springframework.data.mongodb.core.mapping.Field;

/** 확장을 고려해서 일단 document로 저장 */
public class FeedbackPerPage {

  // 페이지 별 피드백인데 이거 필드를 여러개 지정해서 하는것보다 하나의 큰 텍스트로 요구하는게 더 쉬울듯
  @Field("content")
  private String content;
}
