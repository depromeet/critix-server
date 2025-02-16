package depromeet.onepiece.feedback.command.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public record ChatGPTResponse(
    @Schema(description = "챗봇 응답", requiredMode = RequiredMode.REQUIRED) String response) {}
