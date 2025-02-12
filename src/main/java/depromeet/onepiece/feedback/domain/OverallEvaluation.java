package depromeet.onepiece.feedback.domain;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Field;

public class OverallEvaluation {

  @Field("pros")
  private List<String> pros;

  @Field("cons")
  private List<String> cons;

  @Field("one_line_comment")
  private String oneLineComment;

  @Field("grade")
  private String grade;

  @Field("evaluation_list")
  private List<EvaluationItem> evaluationItemList;
}
