package depromeet.onepiece.common.auth.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenProvider {
  private final TokenProperties tokenProperties;

  public String generateAccessToken(String externalId) {
    long currentTimeMillis = System.currentTimeMillis();
    Date now = new Date(currentTimeMillis);
    Date expiration =
        new Date(currentTimeMillis + tokenProperties.expirationTime().accessToken() * 1000);
    SecretKey secretKey = getSigningKey();

    return Jwts.builder()
        .subject(String.valueOf(externalId))
        .issuedAt(now)
        .expiration(expiration)
        .signWith(secretKey)
        .compact();
  }

  public String generateRefreshToken(String externalId) {
    long currentTimeMillis = System.currentTimeMillis();
    Date now = new Date(currentTimeMillis);
    SecretKey secretKey = getSigningKey();

    return Jwts.builder()
        .subject(String.valueOf(externalId))
        .issuedAt(now)
        .signWith(secretKey)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Authentication getAuthentication(String token) {
    Claims claims = getClaims(token);
    Set<SimpleGrantedAuthority> authorities =
        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    return new UsernamePasswordAuthenticationToken(
        new org.springframework.security.core.userdetails.User(
            claims.getSubject(), "", authorities),
        token,
        authorities);
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(this.getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(tokenProperties.secretKey());
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
