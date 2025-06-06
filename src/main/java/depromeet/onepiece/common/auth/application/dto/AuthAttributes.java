package depromeet.onepiece.common.auth.application.dto;

import depromeet.onepiece.user.domain.OAuthProviderType;
import java.util.Map;

public interface AuthAttributes {

  String getExternalId();

  OAuthProviderType getProvider();

  String getEmail();

  String getName();

  String getProfileImageUrl();

  static AuthAttributes of(String providerId, Map<String, Object> attributes) {
    if (OAuthProviderType.GOOGLE.isProviderOf(providerId)) {
      return GoogleAuthAttributes.of(attributes);
    }
    throw new IllegalArgumentException("Unsupported id: " + providerId);
  }
}
