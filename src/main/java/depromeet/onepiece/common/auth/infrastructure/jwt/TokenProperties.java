package depromeet.onepiece.common.auth.infrastructure.jwt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "spring.security.jwt")
public record TokenProperties(
    @NotNull String secretKey,
    @NestedConfigurationProperty @NotNull ExpirationTime expirationTime) {

  public record ExpirationTime(@Min(0) long accessToken, @Min(0) long refreshToken) {}
}
