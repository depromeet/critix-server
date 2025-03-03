package depromeet.onepiece.common.auth.infrastructure.jwt;

import static io.jsonwebtoken.io.Decoders.BASE64;

import depromeet.onepiece.common.auth.domain.jwt.TokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider implements TokenProvider {

  private final TokenProperties tokenProperties;

  @Override
  public String generateAccessToken(String externalId) {
    long currentTimeMillis = System.currentTimeMillis();
    Date now = new Date(currentTimeMillis);
    Date expiration =
        new Date(currentTimeMillis + tokenProperties.expirationTime().accessToken() * 1000);
    SecretKey secretKey = Keys.hmacShaKeyFor(BASE64.decode(tokenProperties.secretKey()));

    return Jwts.builder()
        .subject(String.valueOf(externalId))
        .issuedAt(now)
        .expiration(expiration)
        .signWith(secretKey)
        .compact();
  }

  @Override
  public String generateRefreshToken(String externalId) {
    long currentTimeMillis = System.currentTimeMillis();
    Date now = new Date(currentTimeMillis);
    SecretKey secretKey = Keys.hmacShaKeyFor(BASE64.decode(tokenProperties.secretKey()));

    return Jwts.builder()
        .subject(String.valueOf(externalId))
        .issuedAt(now)
        .signWith(secretKey)
        .compact();
  }
}
