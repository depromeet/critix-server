package depromeet.onepiece.feedback.command.infrastructure;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.NullValidator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackService {

  private final AzureService azureService;
  private final ChatGPTProperties chatGPTProperties;
  private final NullValidator nullValidator;

  public void portfolioFeedback(String fileId, String additionalChat) {
    List<String> fineUrls = getFileUrl(fileId);
    if (additionalChat != null) {
      additionalChat = additionalChat;
    } else {
      additionalChat = "";
    }
    // overall feedback 호출
    overallFeedback(fineUrls, additionalChat);

    // project feedback 호출
    projectFeedback(fineUrls, additionalChat);

    // 응답 합치기

  }

  // overallFeedback 메서드
  public void overallFeedback(List<String> fineUrls, String additionalChat) {

    // AzureService의 processAssistantRequest 메서드 호출
    azureService.processChat(fineUrls, ChatGPTConstants.OVERALL_PROMPT, additionalChat);
  }

  // projectFeedback 메서드
  public void projectFeedback(List<String> fineUrls, String additionalChat) {
    // AzureService의 processAssistantRequest 메서드 호출
    azureService.processChat(fineUrls, ChatGPTConstants.PROJECT_PROMPT, additionalChat);
  }

  // 응답 합치기
  public void mergeResponse() {
    // 응답 합치기
  }

  // DB에서 파일 Url 가져오는 메서드
  public List<String> getFileUrl(String fileId) {
    // DB에서 fileId에 해당하는 파일 URL 가져오기
    return null;
  }
}
