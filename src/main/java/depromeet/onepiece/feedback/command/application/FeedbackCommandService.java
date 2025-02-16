package depromeet.onepiece.feedback.command.application;

import depromeet.onepiece.feedback.command.infrastructure.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackCommandService {
  private final ChatGPTService chatGPTService;

  public PortfolioResponse portfolioFeedback(String portfolioId) {
    return chatGPTService.analyzePortfolio();
  }
}
