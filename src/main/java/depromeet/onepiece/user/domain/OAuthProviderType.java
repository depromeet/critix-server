package depromeet.onepiece.user.domain;

import java.util.Objects;

public enum OAuthProviderType {
  GOOGLE;

  public boolean isProviderOf(String providerId) {
    return Objects.equals(this.name(), providerId);
  }
}
