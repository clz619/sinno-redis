package win.sinno.redis.annotation;

import java.lang.annotation.*;

/**
 * redis hash map
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/6/24 上午9:38
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface HashColumn {

    /**
     * hash map column 的名称
     *
     * @return
     */
    String name();

}
