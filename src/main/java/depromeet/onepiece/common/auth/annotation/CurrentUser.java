package depromeet.onepiece.common.auth.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.swagger.v3.oas.annotations.Parameter;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @CurrentUser 어노테이션은 현재 로그인한 사용자의 정보를 주입받음
 */
@Parameter(hidden = true)
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface CurrentUser {}
