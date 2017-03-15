package win.sinno.redis.interfaces;

import java.util.Set;

/**
 * @author lizhong.chen
 * @version V1.0
 * @data 2016-07-08下午1:41:10
 * @description redis 基础 命令
 */
public interface IRedisBaseCommand {
    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 设置key值
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * set nx px key
     *
     * @param key
     * @param value
     * @param time
     */
    void setNxPx(String key, String value, long time);

    /**
     * 删除keys的对象
     *
     * @param keys
     * @return
     */
    Long del(String... keys);

    /**
     * 设置key的过期时间为seconds-倒计时
     *
     * @param key
     * @param seconds
     * @return
     */
    Long expire(String key, int seconds);

    /**
     * 设置key的过期时间为unixTime-定时在时间戳
     *
     * @param key
     * @param unixTime
     * @return
     */
    Long expireAt(String key, long unixTime);

    /**
     * 查询keys
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 整数原子减1
     *
     * @param key
     * @return
     */
    Long decr(String key);

    /**
     * 整数原子减decrement
     *
     * @param key
     * @param decrement
     * @return
     */
    Long decrBy(String key, Long decrement);


    /**
     * 执行原子加1操作
     *
     * @param key
     * @return
     */
    Long incr(String key);

    /**
     * 执行原子增加一个整数
     *
     * @param key
     * @param increment
     * @return
     */
    Long incrBy(String key, int increment);
}
