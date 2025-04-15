package depromeet.onepiece.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RedisPrefix {

  public static final String RATE_LIMIT_KEY = "rate_limit:";
  public static final String RATE_LIMIT_MAX_REQUEST = "rate_limit:max_request";
  public static final String AI_INCLUDE_IMAGE_FLAG = "image_include_flag";
  public static final String MAXIMUM_PAGE_SIZE = "maximum_page_size";
}
