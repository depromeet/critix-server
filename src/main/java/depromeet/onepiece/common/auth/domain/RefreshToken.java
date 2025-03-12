package depromeet.onepiece.common.auth.domain;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24)
@Builder
@AllArgsConstructor(access = PRIVATE)
public class RefreshToken {
  @Id private String externalId;

  @Indexed private String token;

  public static RefreshToken of(String externalId, String token) {
    return RefreshToken.builder().externalId(externalId).token(token).build();
  }

  public void rotate(String token) {
    this.token = token;
  }
}
