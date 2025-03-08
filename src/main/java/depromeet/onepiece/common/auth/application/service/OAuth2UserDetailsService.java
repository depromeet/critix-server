package depromeet.onepiece.common.auth.application.service;

import depromeet.onepiece.common.auth.domain.CustomUserDetails;
import depromeet.onepiece.user.domain.User;
import depromeet.onepiece.user.query.domain.UserQueryRepository;
import depromeet.onepiece.user.query.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserDetailsService implements UserDetailsService {
  private final UserQueryRepository userQueryRepository;

  @Override
  public UserDetails loadUserByUsername(String externalId) throws UsernameNotFoundException {
    User user =
        userQueryRepository
            .findUserByExternalId(externalId)
            .orElseThrow(UserNotFoundException::new);
    return new CustomUserDetails(user.getId());
  }
}
