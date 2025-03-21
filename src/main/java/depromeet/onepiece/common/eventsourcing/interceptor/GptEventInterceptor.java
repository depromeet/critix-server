package depromeet.onepiece.common.eventsourcing.interceptor;

import static depromeet.onepiece.feedback.domain.FeedbackStatus.PENDING;

import depromeet.onepiece.common.eventsourcing.dto.GptFeedbackStatusTopic;
import depromeet.onepiece.common.eventsourcing.service.KafkaGptEventProducer;
import depromeet.onepiece.user.query.application.CurrentUserIdService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class GptEventInterceptor implements HandlerInterceptor {
  private final CurrentUserIdService currentUserIdService;
  private final KafkaGptEventProducer kafkaGptEventProducer;

  @Value("${spring.kafka.intercept-uri}")
  private String interceptUri;

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (request.getRequestURI().equals(interceptUri)) {
      String fileIdParam = request.getParameter("fileId");
      if (fileIdParam == null || fileIdParam.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return false;
      }

      ObjectId userId =
          currentUserIdService.getCurrentUserId(
              Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                  .map(Authentication::getName)
                  .orElse(null));
      ObjectId fileId = new ObjectId(fileIdParam);

      kafkaGptEventProducer.produceTopic(
          GptFeedbackStatusTopic.of(
              userId, fileId, PENDING, PENDING, 0)); // TODO: retry count 받아오기 필요함
    }
    return true;
  }
}
