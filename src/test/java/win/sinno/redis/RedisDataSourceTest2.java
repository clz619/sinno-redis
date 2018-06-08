package win.sinno.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisMovedDataException;

/**
 * Redis manager test
 *
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2017/1/10 上午11:39
 */
public class RedisDataSourceTest2 {

  public static String host = "127.0.0.1";
  public static int port = 6379;
  public static int timeout = 5000;
  public static String password = "clz619";
  public static int database = 0;


  @Test
  public void testRedisManager() {

    RedisDataSource redisDataSource = new RedisDataSource(new JedisPoolConfig(), host, port,
        timeout, password, database);

//    List<String> mobiles = new ArrayList<>();
//
//    String longUrl = "https://www.cnblogs.com/yanwei-wang/p/5527453.html";
//    for (int i = 0; i < 1000000; i++) {
////      long mb = 13200000000l;
////      mobiles.add(String.valueOf(mb + i));
//      int j = 100000;
//
//      redisDataSource
//          .sadd("SURL:" + j, longUrl + i);
//    }

    for (int i = 0; i < 5000000; i++) {
      redisDataSource.set("SURL:" + i, "abcdef");
    }


  }


  @Test
  public void testRedisManager2() {

    RedisDataSource redisDataSource1 = new RedisDataSource(new JedisPoolConfig(), "127.0.0.1", 6701,
        timeout, null, database);

    RedisDataSource redisDataSource2 = new RedisDataSource(new JedisPoolConfig(), "127.0.0.1", 6702,
        timeout, null, database);

    RedisDataSource redisDataSource3 = new RedisDataSource(new JedisPoolConfig(), "127.0.0.1", 6703,
        timeout, null, database);

    String k1 = "192.168.11.213:8379";
    String k2 = "192.168.11.214:8379";
    String k3 = "192.168.11.215:8379";
    Map<String, RedisDataSource> map = new HashMap<>();
    map.put(k1, redisDataSource1);
    map.put(k2, redisDataSource2);
    map.put(k3, redisDataSource3);

    String k = k1;
    try {
      String v = map.get(k2).get("SURL:ckZgTa");
      System.out.println(v);
    } catch (JedisMovedDataException e) {
      String msg = e.getMessage();
      if (msg.endsWith(k1)) {
        k = k1;
      }
      if (msg.endsWith(k2)) {
        k = k2;
      }
      if (msg.endsWith(k3)) {
        k = k3;
      }
      System.out.println(e.getMessage());

      String v = map.get(k).get("SURL:ckZgTa");
      System.out.println(v);

    }


  }


  @Test
  public void t3() {
    RedisDataSource redisDataSource = new RedisDataSource(new JedisPoolConfig(), "127.0.0.1", 6379,
        6000000, "clz619", database);

    long bts = System.currentTimeMillis();
    Set<String> keys = redisDataSource.keys("*");
    long ets = System.currentTimeMillis();

    System.out.println("get keys useTs:" + (ets - bts) + "ms");



  }

}
