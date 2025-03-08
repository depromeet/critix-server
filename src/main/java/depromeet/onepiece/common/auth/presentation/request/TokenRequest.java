package depromeet.onepiece.common.auth.presentation.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenRequest(
    @Schema(
            description = "리프레시 토큰",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyfQ",
            requiredMode = REQUIRED)
        String refreshToken) {}
