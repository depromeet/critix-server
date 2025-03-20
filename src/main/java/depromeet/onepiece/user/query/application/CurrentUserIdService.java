package depromeet.onepiece.user.query.application;

import depromeet.onepiece.user.query.domain.UserQueryRepository;
import depromeet.onepiece.user.query.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserIdService {
  private final UserQueryRepository userQueryRepository;

  public ObjectId getCurrentUserId(String externalId) {
    return userQueryRepository
        .findUserByExternalId(externalId)
        .orElseThrow(UserNotFoundException::new)
        .getId();
  }
}
