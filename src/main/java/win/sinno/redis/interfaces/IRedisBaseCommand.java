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
    String set(String key, String value);

    /**
     * 将key设置值为value，如果key不存在，这种情况下等同SET命令。 当key存在时，什么也不做。SETNX是”SET if Not eXists”的简写。
     * <p>
     * 返回
     * 1 如果key被设置了
     * 0 如果key没有被设置
     *
     * @param key
     * @param value
     * @return
     */
    Long setnx(String key, String value);

    /**
     * 设置key对应字符串value，并且设置key在给定的seconds时间之后超时过期。
     *
     * @param key
     * @param seconds
     * @param value
     */
    String setex(String key, int seconds, String value);

    /**
     * set value nxxx expx time
     * <p>
     * param nxxx NX|XX  是否存在
     * NX -- Only set the key if it does not already exist.
     * XX -- Only set the key if it already exist.
     * param expx EX|PX, expire time units ，时间单位格式，秒或毫秒
     * EX = seconds;
     * PX = milliseconds
     * <p>
     * set nx px key
     *
     * @param key
     * @param value
     * @param milliseconds
     */
    String setnxpx(String key, String value, long milliseconds);

    String setnxex(String key, String value, long seconds);

    String setxxpx(String key, String value, long milliseconds);

    String setxxex(String key, String value, long seconds);

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
