package depromeet.onepiece.common.auth.domain.jwt;

public record LoginResult(
    String accessToken, String refreshToken, boolean isNewUser, String externalId) {}
