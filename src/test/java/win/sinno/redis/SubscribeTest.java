package win.sinno.redis;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

/**
 * win.sinno.redis.SubscribeTest
 *
 * @author chenlizhong@qipeng.com
 * @date 2018/6/6
 */
public class SubscribeTest {

  public static String host = "localhost";
  public static int port = 6379;
  public static int timeout = 5000;
  public static String password = "clz619";
  public static int database = 1;

  RedisDataSource redisDataSource;

  private String channelName = "sinno";

  JedisClusterInst inst = new JedisClusterInst();


  @Before
  public void before() {
//    redisDataSource = new RedisDataSource(new JedisPoolConfig(), host, port,
//        timeout, password, database);

    String hnps = "127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381,127.0.0.1:6382";
    String pass = "cluster";

    inst.setHostAndPorts(hnps);
//    inst.setPassword(pass);
    inst.setMaxTotal(8);
    inst.setMaxIdle(8);
    inst.init();
  }


  @Test
  public void sub() {
    Jedis jedis = redisDataSource.getJedis();

    RedisMsgPubSubListener listener = new RedisMsgPubSubListener();

    jedis.subscribe(listener, channelName);
  }

  @Test
  public void pub() {
    Jedis jedis = redisDataSource.getJedis();
    jedis.publish(channelName, "hello");
    jedis.publish(channelName, "world");
    jedis.publish(channelName, "redis");
  }

  @Test
  public void subCluster() {
    RedisMsgPubSubListener listener = new RedisMsgPubSubListener();

    JedisCluster jc = inst.getJedisCluster();

    jc.subscribe(listener, channelName);
  }

  @Test
  public void pubCluster() {

    JedisCluster jc = inst.getJedisCluster();

    jc.publish(channelName, "hello");
    jc.publish(channelName, "world");
    jc.publish(channelName, "redis");
    

  }


  @After
  public void stop() throws IOException {

    if (redisDataSource != null) {
      redisDataSource.close();
    }

    if (inst != null) {
      inst.close();
    }
  }

  static class RedisMsgPubSubListener extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
      System.out.println("onMessage channel:" + channel + " , message:" + message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
      System.out.println(
          "onSubscribe channel:" + channel + " , subscribedChannels:" + subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
      System.out.println(
          "onUnsubscribe channel:" + channel + " , subscribedChannels:" + subscribedChannels);
    }
  }
}
