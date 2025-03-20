package depromeet.onepiece.common.auth.application.dto;

import static depromeet.onepiece.user.domain.OAuthProviderType.GOOGLE;

import depromeet.onepiece.user.domain.OAuthProviderType;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleAuthAttributes implements AuthAttributes {
  private final String externalId;
  private final String email;
  private final String name;
  private final String profileImageUrl;
  private final OAuthProviderType provider;

  public static GoogleAuthAttributes of(Map<String, Object> attributes) {
    String externalId = (String) attributes.get("sub");
    String email = (String) attributes.get("email");
    String name = (String) attributes.get("name");
    String profileImageUrl = (String) attributes.get("picture");
    return new GoogleAuthAttributes(externalId, email, name, profileImageUrl, GOOGLE);
  }

  @Override
  public String getExternalId() {
    return this.externalId;
  }

  @Override
  public OAuthProviderType getProvider() {
    return this.provider;
  }

  @Override
  public String getEmail() {
    return this.email;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getProfileImageUrl() {
    return this.profileImageUrl;
  }
}
