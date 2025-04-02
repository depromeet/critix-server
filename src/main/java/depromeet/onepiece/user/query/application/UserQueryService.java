package depromeet.onepiece.user.query.application;

import depromeet.onepiece.user.domain.User;
import depromeet.onepiece.user.query.domain.UserQueryRepository;
import depromeet.onepiece.user.query.exception.UserNotFoundException;
import depromeet.onepiece.user.query.presentation.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {
  private final UserQueryRepository userQueryRepository;

  public UserResponse getCurrentUser(ObjectId userId) {
    User user = getUserById(userId);
    return UserResponse.from(user);
  }

  public User getUserById(ObjectId userId) {
    return userQueryRepository.findUserById(userId).orElseThrow(UserNotFoundException::new);
  }
}
