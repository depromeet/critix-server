package depromeet.onepiece.feedback.command.infrastructure;

import depromeet.onepiece.feedback.command.presentation.request.ChatGPTRequest;
import depromeet.onepiece.feedback.command.presentation.response.ChatGPTResponse;
import depromeet.onepiece.feedback.command.presentation.response.OverallFeedbackResponse;
import depromeet.onepiece.feedback.command.presentation.response.ProjectFeedbackResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGPTService {
  private final ChatGPTClient chatGPTClient;

  public OverallFeedbackResponse overallFeedback(ChatGPTRequest requestDto) {
    ChatGPTRequest updatedRequest =
        new ChatGPTRequest(
            requestDto.model(),
            requestDto.messages(),
            requestDto.response_format(),
            0.7,
            5000,
            1.0,
            0,
            0);

    ChatGPTResponse response = chatGPTClient.sendMessage(updatedRequest);
    return mapToOverallFeedback(response);
  }

  public ProjectFeedbackResponse projectFeedback(ChatGPTRequest requestDto) {
    ChatGPTRequest updatedRequest =
        new ChatGPTRequest(
            requestDto.model(),
            requestDto.messages(),
            requestDto.response_format(),
            0.8,
            5000,
            1.0,
            0,
            0);

    ChatGPTResponse response = chatGPTClient.sendMessage(updatedRequest);
    return mapToProjectFeedback(response);
  }

  // TODO: response 수정 후 매핑
  private OverallFeedbackResponse mapToOverallFeedback(ChatGPTResponse response) {
    return new OverallFeedbackResponse(
        new OverallFeedbackResponse.OverallEvaluation(
            response.response(),
            new OverallFeedbackResponse.EvaluationDetail(80, "직무 적합성이 높습니다."),
            new OverallFeedbackResponse.EvaluationDetail(75, "논리적인 사고력이 돋보입니다."),
            new OverallFeedbackResponse.EvaluationDetail(85, "글쓰기 표현이 명확합니다."),
            new OverallFeedbackResponse.EvaluationDetail(90, "레이아웃이 가독성이 높습니다.")),
        List.of(new OverallFeedbackResponse.FeedbackDetail("창의적인 디자인", "다양한 시각적 요소 활용이 뛰어남")),
        List.of(new OverallFeedbackResponse.FeedbackDetail("문장 간결화 필요", "텍스트를 조금 더 다듬으면 좋습니다.")));
  }

  // TODO: response 수정 후 매핑
  private ProjectFeedbackResponse mapToProjectFeedback(ChatGPTResponse response) {
    return new ProjectFeedbackResponse(
        new ProjectFeedbackResponse.ProjectEvaluation(
            "프로젝트 이름", "https://example.com/image.png", null, "프로세스 리뷰", "강점 분석", "개선 사항", null));
  }
}
