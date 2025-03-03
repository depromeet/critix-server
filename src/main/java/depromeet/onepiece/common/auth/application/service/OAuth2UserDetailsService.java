package depromeet.onepiece.common.auth.application.service;

import depromeet.onepiece.common.auth.domain.CustomUserDetails;
import depromeet.onepiece.user.domain.User;
import depromeet.onepiece.user.query.domain.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2UserDetailsService implements UserDetailsService {

  private final UserQueryRepository userQueryRepository;

  @Override
  public UserDetails loadUserByUsername(String externalId) throws UsernameNotFoundException {
    User user =
        userQueryRepository
            .findUserByExternalId(externalId)
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + externalId));
    return new CustomUserDetails(user.getId());
  }
}
