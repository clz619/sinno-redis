package win.sinno.redis.annotation;


import win.sinno.redis.interfaces.ISortedSetScoreHandler;

import java.lang.annotation.*;

/**
 * 标识为redis set集合对象
 *
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2016-10-11 11:39.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface SortedSet {

    /**
     * set集合的key
     *
     * @return
     */
    String key();

    /**
     * 分数处理器
     *
     * @return
     */
    Class<? extends ISortedSetScoreHandler> scoreHandler();
}
