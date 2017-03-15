package win.sinno.redis;

import org.junit.Test;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis manager test
 *
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2017/1/10 上午11:39
 */
public class RedisManagerTest {

    public static String host = "10.1.1.131";
    public static int port = 6379;
    public static int timeout = 5000;
    public static String password = "redis";
    public static int database = 2;


    @Test
    public void testRedisManager() {

        RedisManager redisManager = new RedisManager(new JedisPoolConfig(), host, port, timeout, password, database);

        redisManager.set("name", "ccc");

        System.out.println(redisManager.get("name"));
    }

}
