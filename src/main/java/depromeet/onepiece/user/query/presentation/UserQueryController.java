package depromeet.onepiece.user.query.presentation;

import depromeet.onepiece.common.auth.annotation.CurrentUserId;
import depromeet.onepiece.common.error.CustomResponse;
import depromeet.onepiece.user.query.application.UserQueryService;
import depromeet.onepiece.user.query.presentation.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserQueryController {
  private final UserQueryService userQueryService;

  @GetMapping("/me")
  @Operation(summary = "현재 로그인한 사용자 정보 조회", description = "현재 로그인한 사용자 정보 조회 API [담당자 : 이한음]")
  public CustomResponse<UserResponse> me(@CurrentUserId ObjectId currentUserId) {
    UserResponse response = userQueryService.getCurrentUser(currentUserId);
    return new CustomResponse<>(response);
  }
}
