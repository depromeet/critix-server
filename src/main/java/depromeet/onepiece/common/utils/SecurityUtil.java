package depromeet.onepiece.common.utils;

import depromeet.onepiece.common.auth.presentation.exception.AuthenticationRequiredException;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

  public static ObjectId getCurrentMemberId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    try {
      return new ObjectId(authentication.getName());
    } catch (Exception e) {
      throw new AuthenticationRequiredException();
    }
  }
}
