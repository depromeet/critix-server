package depromeet.onepiece.feedback.command.presentation.request;

import java.util.List;
import java.util.Map;

public record ChatGPTRequestDto(
    String model,
    List<Message> messages,
    ResponseFormat response_format,
    Double temperature,
    Integer max_completion_tokens,
    Double top_p,
    Integer frequency_penalty,
    Integer presence_penalty) {
  public record Message(String role, List<Content> content) {
    public record Content(String type, String text, ImageUrl image_url) {
      public record ImageUrl(String url) {}
    }
  }

  public record ResponseFormat(String type, JsonSchema json_schema) {
    public record JsonSchema(String name, Boolean strict, Map<String, Object> schema) {}
  }
}
