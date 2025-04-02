package depromeet.onepiece.user.query.presentation.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import depromeet.onepiece.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserResponse(
    @Schema(description = "사용자 이메일", example = "example@gmail.com", requiredMode = REQUIRED)
        String email,
    @Schema(description = "사용자 이름", example = "홍길동", requiredMode = REQUIRED) String name,
    @Schema(
            description = "사용자 프로필 이미지 URL",
            example = "https://example.com/profile.jpg",
            requiredMode = REQUIRED)
        String profileImageUrl) {
  public static UserResponse from(User user) {
    return UserResponse.builder()
        .email(user.getEmail())
        .name(user.getName())
        .profileImageUrl(user.getProfileImageUrl())
        .build();
  }
}
