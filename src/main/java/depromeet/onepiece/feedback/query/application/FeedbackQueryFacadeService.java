package depromeet.onepiece.feedback.query.application;

import depromeet.onepiece.common.utils.EncryptionUtil;
import depromeet.onepiece.common.utils.RedisPrefix;
import depromeet.onepiece.feedback.domain.Feedback;
import depromeet.onepiece.feedback.domain.FeedbackPerPage;
import depromeet.onepiece.feedback.domain.ProjectEvaluation;
import depromeet.onepiece.feedback.query.presentation.response.FeedbackDetailResponse;
import depromeet.onepiece.feedback.query.presentation.response.RecentFeedbackListResponse;
import depromeet.onepiece.file.command.application.PresignedUrlGenerator;
import depromeet.onepiece.file.domain.FileDocument;
import depromeet.onepiece.file.query.application.FileQueryService;
import java.util.ArrayList;
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
    List<RecentFeedbackListResponse> list = new ArrayList<>();
    for (Feedback feedback1 : feedbackList) {
      if (feedback1.getOverallEvaluation() != null && feedback1.getProjectEvaluation() != null) {
        String title = getFileTitle(feedback1.getFileId(), fileMap);
        RecentFeedbackListResponse apply =
            new RecentFeedbackListResponse(
                feedback1.getId(), feedback1.getCreatedAt().toLocalDate(), title);
        list.add(apply);
      }
    }
    return list;
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
    List<ProjectEvaluation> projectEvaluation = feedback.getProjectEvaluation();
    if (projectEvaluation != null) {
      for (ProjectEvaluation evaluation : projectEvaluation) {
        List<FeedbackPerPage> feedbackPerPage = evaluation.getFeedbackPerPage();
        List<FeedbackPerPage> notEmptyList = new ArrayList<>();
        for (FeedbackPerPage perPage : feedbackPerPage) {
          if (perPage.getContents().size() > 0) {
            notEmptyList.add(perPage);
          }
          evaluation.setFeedbackPerPage(notEmptyList);
        }
      }
    }

    updateImageUrls(feedback);
    List<String> imageList =
        presignedUrlGenerator.generatePresignedUrl(feedback.getFileId().toString());
    return new FeedbackDetailResponse(feedback, imageList);
  }

  private void updateImageUrls(Feedback feedback) {
    if (feedback.getProjectEvaluation() == null) {
      log.warn("프로젝트 평가 정보가 없습니다. 피드백 ID: {}", feedback.getId());
      return;
    }

    feedback
        .getProjectEvaluation()
        .forEach(
            projectEvaluation -> {
              if (projectEvaluation.getFeedbackPerPage() != null) {
                projectEvaluation
                    .getFeedbackPerPage()
                    .forEach(
                        feedbackPerPage -> {
                          String pageNumber = feedbackPerPage.getPageNumber();
                          if (pageNumber != null) {
                            String objectKey =
                                feedback.getFileId().toString() + "/processed/" + pageNumber;
                            try {
                              String presignedUrl =
                                  presignedUrlGenerator.generatePresignedUrlForKey(objectKey);
                              if (presignedUrl != null && !presignedUrl.isEmpty()) {
                                feedbackPerPage.updateImageUrl(presignedUrl);
                              } else {
                                log.warn("Presigned URL이 비어 있습니다: {}", objectKey);
                              }
                            } catch (Exception e) {
                              log.error("Presigned URL 생성 중 오류 발생: {}", objectKey, e);
                            }
                          }
                        });
              }

              String projectImageUrl = projectEvaluation.getProjectImageUrl();
              if (projectImageUrl != null && !projectImageUrl.isEmpty()) {
                String objectKey =
                    feedback.getFileId().toString() + "/processed/" + projectImageUrl;
                try {
                  String presignedUrl = presignedUrlGenerator.generatePresignedUrlForKey(objectKey);
                  if (presignedUrl != null && !presignedUrl.isEmpty()) {
                    projectEvaluation.updatePageImageUrl(presignedUrl);
                  } else {
                    log.warn("프로젝트 대표 이미지 필드가 비어 있습니다: {}", presignedUrl);
                  }
                } catch (Exception e) {
                  log.error("프로젝트 대표 이미지 Presigned URL 생성 중 오류 발생: {}", projectImageUrl, e);
                }
              }
            });
  }
}
