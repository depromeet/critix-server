package depromeet.onepiece.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RedisPrefix {

  public static final String RATE_LIMIT_KEY = "rate_limit:";
  public static final String RATE_LIMIT_MAX_REQUEST = "rate_limit:max_request";
}
