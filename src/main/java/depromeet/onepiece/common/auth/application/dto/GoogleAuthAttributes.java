package depromeet.onepiece.common.auth.application.dto;

import depromeet.onepiece.user.domain.OAuthProviderType;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleAuthAttributes implements AuthAttributes {

  private final String id;
  private final String email;
  private final OAuthProviderType provide;

  public static GoogleAuthAttributes of(Map<String, Object> attributes) {
    Map<String, Object> google = (Map<String, Object>) attributes.get("google");

    return new GoogleAuthAttributes(
        attributes.get("sub").toString(), (String) google.get("email"), OAuthProviderType.GOOGLE);
  }

  @Override
  public String getExternalId() {
    return this.id;
  }

  @Override
  public String getEmail() {
    return this.email;
  }

  @Override
  public OAuthProviderType getProvider() {
    return this.provide;
  }
}
