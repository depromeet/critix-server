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
    String id = (String) attributes.get("sub");
    String email = (String) attributes.get("email");
    return new GoogleAuthAttributes(id, email, OAuthProviderType.GOOGLE);
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
