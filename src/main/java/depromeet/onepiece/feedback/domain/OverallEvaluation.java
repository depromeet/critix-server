package depromeet.onepiece.feedback.domain;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
  private List<depromeet.onepiece.feedback.domain.EvaluationItem> evaluationItemList;
}
