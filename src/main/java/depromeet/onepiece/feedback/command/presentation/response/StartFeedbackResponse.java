package depromeet.onepiece.feedback.command.presentation.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import depromeet.onepiece.feedback.domain.FeedbackStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record StartFeedbackResponse(
    @Schema(description = "feedbackId", requiredMode = REQUIRED) String feedbackId,
    @Schema(description = "status", requiredMode = REQUIRED) FeedbackStatus status) {}
