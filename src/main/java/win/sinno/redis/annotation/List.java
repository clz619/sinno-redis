package win.sinno.redis.annotation;

import java.lang.annotation.*;

/**
 * 标识为redis list
 * <p>
 * key为列表对应的关键字
 *
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2016-10-11 11:31.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface List {

    String key();
}
