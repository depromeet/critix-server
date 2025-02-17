package depromeet.onepiece.feedback.command.presentation.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatGPTResponse(
    @Schema(description = "챗봇 응답", requiredMode = REQUIRED) String response) {}
