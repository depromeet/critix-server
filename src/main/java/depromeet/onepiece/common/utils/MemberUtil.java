package depromeet.onepiece.common.utils;

import depromeet.onepiece.user.domain.User;
import depromeet.onepiece.user.domain.UserRepository;
import depromeet.onepiece.user.query.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberUtil {

  private final SecurityUtil securityUtil;
  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public User getCurrentMember() {
    return userRepository
        .findById(securityUtil.getCurrentMemberId().toString())
        .orElseThrow(UserNotFoundException::new);
  }
}
