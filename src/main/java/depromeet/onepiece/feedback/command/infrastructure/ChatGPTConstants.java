package depromeet.onepiece.feedback.command.infrastructure;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ChatGPTConstants {
  public static final String API_URL = "https://api.openai.com/v1/chat/completions";
  public static final String OVERALL_PROMPT = "전체피드백";
  public static final String PROJECT_PROMPT = "프로젝트 별 피드백";
}
