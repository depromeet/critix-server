package depromeet.onepiece.common.auth.application.jwt;

public record TokenResult(String refreshToken, boolean isNewUser, String externalId) {}
