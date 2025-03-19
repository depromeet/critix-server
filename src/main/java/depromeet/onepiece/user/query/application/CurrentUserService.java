package depromeet.onepiece.user.query.application;

import depromeet.onepiece.user.domain.User;
import depromeet.onepiece.user.query.exception.UserNotAuthenticatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrentUserService {
  private final UserQueryService userQueryService;

  public User me() {
    try {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      String userId = ((UserDetails) principal).getUsername();
      return userQueryService.getUserById(userId);
    } catch (Exception e) {
      throw new UserNotAuthenticatedException();
    }
  }
}
