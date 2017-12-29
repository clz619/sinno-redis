package win.sinno.redis;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import win.sinno.io.csv.CsvWriter;

/**
 * com.qipeng.test.BlackCountTest
 *
 * @author chenlizhong@qipeng.com
 * @date 2017/12/18
 */
public class BlackCountTest {

  @Test
  public void black() throws IOException {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    String host = "localhost";
    int port = 6401;
    int timeout = 10000;
    String password = "f11terb1ack";
    int database = 0;

    JedisPool pool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);

    Jedis jedis = pool.getResource();
    Set<String> keySets = jedis.keys("*");

    CsvWriter csvWriter = new CsvWriter();
    csvWriter.setFileName("wk-black-count");
    csvWriter.setOutPath("/Users/chenlizhong/Documents/");
    csvWriter.setAppendMode(true);
    csvWriter.setCharset("utf-8");
    csvWriter.build();
    csvWriter.setBom();

    String[] header = {"key", "数量"};
    csvWriter.append(header);
    csvWriter.newLine();
    csvWriter.flush();

    String[] arrays = new String[2];

    for (String key : keySets) {

      try {
        String[] keyArray = key.split(":");
        if (keyArray.length < 2) {
          continue;
        }
        if (!"black".equals(keyArray[0])) {
          continue;
        }

        String uidStr = keyArray[1];
        Long uid = Long.valueOf(uidStr);
        if (uid >= 890000000000000000l) {
          continue;
        }

        long size = jedis.scard(key);

        System.out.println(key + "   " + size);

        arrays[0] = key;
        arrays[1] = "" + size;
        csvWriter.append(arrays);
        csvWriter.newLine();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    csvWriter.flush();
    csvWriter.close();

  }


  @Test
  public void getBlack() throws IOException {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    String host = "localhost";
    int port = 6401;
    int timeout = 10000;
    String password = "f11terb1ack";
    int database = 0;

    JedisPool pool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);

    Jedis jedis = pool.getResource();
    Set<String> str = jedis.smembers("black:13622455:1:buyers");

    System.out.println(str.size());

    System.out.println("~~~~~~");

    Iterator<String> it = str.iterator();

    while (it.hasNext()) {
      System.out.println(it.next());
    }

  }
}
