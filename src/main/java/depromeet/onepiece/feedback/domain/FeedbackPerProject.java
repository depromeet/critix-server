package depromeet.onepiece.feedback.domain;

import org.springframework.data.mongodb.core.mapping.Field;

public class FeedbackPerProject {

  // 전체 프로젝트 평가
  @Field("project_evaluation")
  private String projectEvaluation;

  // 이런 점이 아쉬워요
  @Field("project_cons")
  private String projectCons;

  // 이런 점이 좋아요
  @Field("project_pros")
  private String projectPros;
}
