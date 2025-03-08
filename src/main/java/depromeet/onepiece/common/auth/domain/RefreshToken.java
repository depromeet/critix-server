package depromeet.onepiece.common.auth.domain;

import static lombok.AccessLevel.PROTECTED;

import depromeet.onepiece.common.auth.presentation.exception.RefreshTokenInvalidException;
import depromeet.onepiece.common.domain.BaseTimeDocument;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Document
@NoArgsConstructor(access = PROTECTED)
public class RefreshToken extends BaseTimeDocument {
  @MongoId private ObjectId id;

  private String externalId;

  private String token;

  private LocalDateTime expiredAt;

  private RefreshToken(String externalId, String token, LocalDateTime expiredAt) {
    this.externalId = externalId;
    this.token = token;
    this.expiredAt = expiredAt;
  }

  public static RefreshToken of(String externalId, String token, long expiredSeconds) {
    LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(expiredSeconds);

    return new RefreshToken(externalId, token, expiredAt);
  }

  public void rotate(String token) {
    this.token = token;
  }

  public void updateExpirationIfExpired(long expiredSeconds) {
    if (expiredAt.isBefore(LocalDateTime.now())) {
      expiredAt = LocalDateTime.now().plusSeconds(expiredSeconds);
    }
  }

  public void validateWith(String token, String externalId) {
    if (isNotMatchedToken(token) || isExpired() || isNotMatchedUserId(externalId)) {
      throw new RefreshTokenInvalidException();
    }
  }

  private boolean isNotMatchedToken(String token) {
    return !Objects.equals(this.token, token);
  }

  private boolean isExpired() {
    return expiredAt.isBefore(LocalDateTime.now());
  }

  private boolean isNotMatchedUserId(String userId) {
    return !Objects.equals(this.externalId, userId);
  }
}
