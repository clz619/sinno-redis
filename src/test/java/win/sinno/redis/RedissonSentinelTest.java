package win.sinno.redis;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * win.sinno.redis.RedissonSentinelTest
 *
 * @author chenlizhong@qipeng.com
 * @date 2017/11/3
 */
public class RedissonSentinelTest {

  private Config config;

  private String masterSentinelName;
  private String masterSentinelList;

  private String masterRedisServer;
  private String masterRedisPass;
  private int masterRedisPort;

  private RedissonClient redisson;

  {
    masterRedisServer = "redis://127.0.0.1";
    masterRedisPort = 6379;

    masterRedisPass = "clz619";
    masterSentinelName = "sinno";
    masterSentinelList = "redis://127.0.0.1:26379,redis://127.0.0.1:26479,redis://127.0.0.1:26579";

    config = new Config();

    if (StringUtils.isNotEmpty(masterSentinelList)) {

      String[] sentinelAddressArray = masterSentinelList.split(",");

      config.useSentinelServers()
          .setMasterName(masterSentinelName)
          .addSentinelAddress(sentinelAddressArray)
          .setPassword(masterRedisPass)
          .setConnectTimeout(5000)
          .setDatabase(0);

    } else {

      config.useSingleServer()
          .setClientName("clientName")
          .setAddress(masterRedisServer + ":" + masterRedisPort)
          .setPassword(masterRedisPass)
          .setConnectTimeout(5000)
          .setConnectionPoolSize(16)
          .setConnectionMinimumIdleSize(8)
          .setDatabase(0);
      
    }

    redisson = Redisson.create(config);

  }

  @Test
  public void testMap() {

    String key = "test";

    RMap map = redisson.getMap("name");
    map.put(key, "aaa");
    System.out.println(map.get(key));

    RKeys rKeys = redisson.getKeys();
    System.out.println(rKeys.count());

  }

}
