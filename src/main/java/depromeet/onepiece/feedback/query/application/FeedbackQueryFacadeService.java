package depromeet.onepiece.feedback.query.application;

import depromeet.onepiece.common.utils.EncryptionUtil;
import depromeet.onepiece.common.utils.RedisPrefix;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.query.presentation.response.FeedbackDetailResponse;
import depromeet.onepiece.feedback.query.presentation.response.RecentFeedbackListResponse;
import depromeet.onepiece.file.command.infrastructure.PresignedUrlGenerator;
import depromeet.onepiece.file.domain.FileDocument;
import depromeet.onepiece.file.query.application.FileQueryService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackQueryFacadeService {
  private final FeedbackQueryService feedbackQueryService;
  private final FileQueryService fileQueryService;
  private final RedisTemplate<String, String> redisTemplate;
  private final PresignedUrlGenerator presignedUrlGenerator;

  public List<RecentFeedbackListResponse> getFeedbackList(ObjectId userId) {
    List<Feedback> feedbackList = feedbackQueryService.findByUserId(userId);
    List<ObjectId> fileIdList = feedbackList.stream().map(Feedback::getFileId).toList();
    Map<ObjectId, FileDocument> fileMap =
        fileQueryService.findAllByIds(fileIdList).stream()
            .collect(Collectors.toMap(FileDocument::getId, Function.identity()));
    return feedbackList.stream()
        .map(
            feedback -> {
              String title = getFileTitle(feedback.getFileId(), fileMap);
              return new RecentFeedbackListResponse(
                  feedback.getId(), feedback.getCreatedAt().toLocalDate(), title);
            })
        .toList();
  }

  private String getFileTitle(ObjectId fileId, Map<ObjectId, FileDocument> fileMap) {
    return Optional.ofNullable(fileMap.get(fileId))
        .map(fileDocument -> EncryptionUtil.decrypt(fileDocument.getLogicalName()))
        .orElse("");
  }

  public Long getRemainCount(ObjectId userId) {
    long size =
        Optional.ofNullable(
                redisTemplate.opsForZSet().size(RedisPrefix.RATE_LIMIT_KEY + userId.toString()))
            .orElse(0L);
    redisTemplate.opsForValue().setIfAbsent(RedisPrefix.RATE_LIMIT_MAX_REQUEST, "10");
    String maxRequest = redisTemplate.opsForValue().get(RedisPrefix.RATE_LIMIT_MAX_REQUEST);
    return Long.parseLong(Objects.requireNonNull(maxRequest)) - size;
  }

  public FeedbackDetailResponse getFeedback(ObjectId feedbackId) {
    Feedback feedback = feedbackQueryService.findById(feedbackId);
    List<String> imageList =
        presignedUrlGenerator.generatePresignedUrl(feedback.getFileId().toString());

    return new FeedbackDetailResponse(feedback, imageList);
  }
}
