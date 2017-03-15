package win.sinno.redis.annotation;

import java.lang.annotation.*;

/**
 * 标识为集合的列
 *
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2016-10-11 11:43.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SortedSetColumn {

    /**
     * 集合列名
     *
     * @return
     */
    String name() default "";
}
