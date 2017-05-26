package win.sinno.redis;

import org.junit.Test;
import win.sinno.common.util.RuntimeUtil;
import win.sinno.redis.idworker.IdWorker4Redis;
import win.sinno.redis.idworker.IdWorker4RedisConfig;

/**
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2017-05-26 17:48.
 */
public class IdWorker4RedisTest {

    @Test
    public void test() {
        IdWorker4RedisConfig config = new IdWorker4RedisConfig();
        config.setClusterName("sinno")
                .setHostName(RuntimeUtil.getRunName())
                .setRedisHost("10.1.1.131")
                .setRedisPort(6379)
                .setRedisPassword("redis")
                .setRedisDatabase(3)
                .setRedisTimeout(5000)
        ;
        IdWorker4Redis idWorker = IdWorker4Redis.newInstance(config);
        for (int i = 0; i < 10; i++) {
            System.out.println(idWorker.nextId());
        }
    }
}
