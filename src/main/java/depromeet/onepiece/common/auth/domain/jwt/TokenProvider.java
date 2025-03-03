package depromeet.onepiece.common.auth.domain.jwt;

public interface TokenProvider {

  String generateAccessToken(String externalId);

  String generateRefreshToken(String externalId);
}
