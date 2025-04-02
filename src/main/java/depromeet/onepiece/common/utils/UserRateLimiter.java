package depromeet.onepiece.common.utils;

import depromeet.onepiece.common.auth.infrastructure.jwt.TokenProvider;
import depromeet.onepiece.common.error.GlobalErrorCode;
import depromeet.onepiece.common.error.GlobalException;
import depromeet.onepiece.user.query.application.CurrentUserIdService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class UserRateLimiter implements HandlerInterceptor {

  public static final String BEARER = "Bearer ";
  private final RedisTemplate<String, String> redisTemplate;
  private final TokenProvider tokenProvider;
  private final CurrentUserIdService currentUserIdService;

  public static final String LUA_SCRIPT =
      """
		-- KEYS[1]  = rate_limit:{userId}
		-- ARGV[1] = current timestamp (ms)
		-- ARGV[2] = window size (ms)
		-- ARGV[3] = max allowed requests

		local key = KEYS[1]
		local now = tonumber(ARGV[1])
		local window = tonumber(ARGV[2])
		local limit = tonumber(ARGV[3])
		local earliest = now - window

		-- 1. 오래된 요청 삭제
		redis.call("ZREMRANGEBYSCORE", key, 0, earliest)

		-- 2. 현재 요청 개수 확인
		local count = redis.call("ZCARD", key)

		-- 3. 요청 허용 여부 판단
		if count < limit then
		    -- 허용: 타임스탬프 삽입하고 OK 반환
		    redis.call("ZADD", key, now, tostring(now))
		    redis.call("PEXPIRE", key, window * 2) -- 만약 TTL이 없다면 설정 (메모리 관리)
		    return 1
		else
		    -- 제한 초과: 요청 거부
		    return 0
		end

		""";

  public boolean isAllowed(String userId) {
    String key = "rate_limit:" + userId;
    long now = System.currentTimeMillis();
    long window = 1000L * 60 * 60 * 24 * 30; // 30일
    redisTemplate.opsForValue().setIfAbsent(RedisPrefix.RATE_LIMIT_MAX_REQUEST, "10");
    String maxRequests = redisTemplate.opsForValue().get(RedisPrefix.RATE_LIMIT_MAX_REQUEST);

    DefaultRedisScript<Long> script = new DefaultRedisScript<>();
    script.setScriptText(LUA_SCRIPT); // 위 Lua 문자열
    script.setResultType(Long.class);

    Long result =
        redisTemplate.execute(
            script,
            Collections.singletonList(key),
            String.valueOf(now),
            String.valueOf(window),
            maxRequests);

    return result == 1;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.startsWith(BEARER)) {
      String token = header.substring(BEARER.length());
      Authentication authentication = tokenProvider.getAuthentication(token);
      ObjectId userId = currentUserIdService.getCurrentUserId(authentication.getName());
      if (isAllowed(userId.toString())) {
        return true;
      } else {
        throw new GlobalException("너무 많은 요청입니다.", GlobalErrorCode.TOO_MANY_REQUESTS);
      }
    }
    return true;
  }
}
