package depromeet.onepiece.user.query.application;

import depromeet.onepiece.user.domain.User;
import depromeet.onepiece.user.query.domain.UserQueryRepository;
import depromeet.onepiece.user.query.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {
  private final UserQueryRepository userQueryRepository;

  public User getUserById(String userId) {
    return userQueryRepository
        .findUserById(new ObjectId(userId))
        .orElseThrow(UserNotFoundException::new);
  }
}
