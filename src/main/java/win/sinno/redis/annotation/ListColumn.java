package win.sinno.redis.annotation;

import java.lang.annotation.*;

/**
 * redis list - json串的列名
 *
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2016-10-11 11:35.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface ListColumn {

    String name() default "";
}
