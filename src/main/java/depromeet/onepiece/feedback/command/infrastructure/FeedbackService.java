package depromeet.onepiece.feedback.command.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackService {

  private final AzureService azureService;

  public void portfolioFeedback(String portfolioId) {
    // overall feedback 호출

    // project feedback 호출

    // 응답 합치기

  }

  // overallFeedback 메서드
  public void overallFeedback() {
    // AzureService의 processAssistantRequest 메서드 호출
    azureService.processAssistantRequest();
  }

  // projectFeedback 메서드
  public void projectFeedback() {
    // AzureService의 processAssistantRequest 메서드 호출
    azureService.processAssistantRequest();
  }

  // 응답 합치기
  public void mergeResponse() {
    // 응답 합치기
  }
}
