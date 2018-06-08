package win.sinno.redis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * Redis manager test
 *
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2017/1/10 上午11:39
 */
public class RedisDataSource4ClusterTest {

  public static String host = "192.168.60.38";
  public static int port = 6379;
  public static int timeout = 5000;
  public static String password = "cluster";
  public static int database = 0;
  private JedisCluster jedisCluster = null;

  @Before
  public void connCluster() {
    Set<HostAndPort> hostAndPorts = new HashSet<>();
    hostAndPorts.add(new HostAndPort(host, 6379));
    hostAndPorts.add(new HostAndPort(host, 6380));
    hostAndPorts.add(new HostAndPort(host, 6381));
    hostAndPorts.add(new HostAndPort(host, 6382));

    jedisCluster = new JedisCluster(hostAndPorts, timeout, timeout, 1, password,
        new GenericObjectPoolConfig());

//    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
//    jedisCluster = new JedisCluster(hostAndPorts);

  }

  @Test
  public void testRedisManager() {

    long bts = System.currentTimeMillis();
    for (int i = 0; i < 100; i++) {
//      jedisCluster.set("name:" + i, System.currentTimeMillis() + "");
//      System.out.println(jedisCluster.get("name:" + i));
      jedisCluster.get("name:" + i);

//      jedisCluster.
    }

    long ets = System.currentTimeMillis();

    System.out.println(ets - bts);

//    Map<String, JedisPool> map = jedisCluster.getClusterNodes();
//    JedisPool jedisPool = map.values().iterator().next();
//    Jedis jedis = jedisPool.getResource();
//    Pipeline pipeline = jedis.pipelined();
//    for (int i = 0; i < 10; i++) {
//      pipeline.set("sms" + i, i + "");
//    }
//    pipeline.sync();

  }

  @After
  public void after() throws IOException {
    jedisCluster.close();
  }

}
