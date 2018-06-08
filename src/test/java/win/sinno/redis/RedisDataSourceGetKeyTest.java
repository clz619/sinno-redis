package win.sinno.redis;


import java.io.IOException;
import java.util.Set;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import win.sinno.io.FileWriter;

/**
 * Redis manager test
 *
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2017/1/10 上午11:39
 */
public class RedisDataSourceGetKeyTest {

  private static final Logger LOG = LoggerFactory.getLogger(RedisDataSourceGetKeyTest.class);

  @Test
  public void testRedisManager() throws IOException {

    RedisDataSource redisDataSource = new RedisDataSource(new JedisPoolConfig(), "127.0.0.1", 6379,
        600000, "clz619", 0);

    long bts = System.currentTimeMillis();
    Set<String> keys = redisDataSource.keys("*");
    long ets = System.currentTimeMillis();

    LOG.info("get keys useTs:{}ms size:{}", (ets - bts), keys.size());

    FileWriter fileWriter = new FileWriter("/Users/chenlizhong", "surlKeys.txt", true);
    fileWriter.build();

    for (String k : keys) {

      if (k.length() == 11 && k.startsWith("SURL:")) {
        fileWriter.write(k);
        fileWriter.newLine();
      }
    }

    fileWriter.flush();

    fileWriter.close();
    redisDataSource.close();

  }


  @Test
  public void testRedisManager1() throws IOException {

    RedisDataSource redisDataSource = new RedisDataSource(new JedisPoolConfig(), "127.0.0.1", 6379,
        600000, "clz619", 0);


  }

}
