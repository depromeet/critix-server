package depromeet.onepiece.common.auth.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "spring.security")
public record SecurityProperties(
    String loginUrl, String redirectUrl, @NestedConfigurationProperty Cookie cookie) {
  public record Cookie(String domain, boolean httpOnly, boolean secure) {}
}
