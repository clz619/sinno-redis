package win.sinno.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisCluster;

/**
 * win.sinno.redis.JedisClusterInstTest
 *
 * @author chenlizhong@qipeng.com
 * @date 2018/4/25
 */
public class JedisClusterInstTest {


  JedisClusterInst inst = new JedisClusterInst();


  @Before
  public void before() {

    String hnps = "127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381,127.0.0.1:6382";
    String pass = "cluster";

    inst.setHostAndPorts(hnps);
//    inst.setPassword(pass);
    inst.setMaxTotal(8);
    inst.setMaxIdle(8);
    inst.init();
  }

  @Test
  public void testLock() {

    JedisCluster jc = inst.getJedisCluster();

    String k = "lock:1234";
    String v = "123";

    String code = jc.set(k, v, "NX", "EX", 86400);
    System.out.println(code);
  }

  @Test
  public void testUnLock() {

    JedisCluster jc = inst.getJedisCluster();

    String k = "lock:1234";
    String v = "123";

    Object obj = jc.eval("if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
        "then\n" +
        "    return redis.call(\"del\",KEYS[1])\n" +
        "else\n" +
        "    return 0\n" +
        "end", Collections.singletonList(k), Collections.singletonList(v));

    System.out.println(obj);
  }


  @Test
  public void test() {

    JedisCluster jc = inst.getJedisCluster();
    String o = jc.get("name:62822");
    System.out.println(o);
//    jc.eval();
  }

  @Test
  public void mutlTest() throws InterruptedException {
    final JedisCluster jc = inst.getJedisCluster();

    List<Thread> ts = new ArrayList<>();

    for (int i = 0; i < 1000000; i++) {
      Thread t = new Thread(new MutiThread("t" + i + ":", jc, 1));
      ts.add(t);
    }

    Iterator<Thread> tit = ts.iterator();
    int i = 0;

    while (tit.hasNext()) {
      Thread t = tit.next();
//      try {
      t.start();
//        t.join();
      System.out.println("done." + (++i));
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
    }
    System.out.println("finish.");

    Thread.sleep(1000000l);


  }

  @Test
  public void mutlTest2() throws InterruptedException {
    final JedisCluster jc = inst.getJedisCluster();

    List<Thread> ts = new ArrayList<>();

    for (int i = 0; i < 100; i++) {
      Thread t = new Thread(new MutiThread("smt" + i + ":", jc, 100000));
      ts.add(t);
      t.start();
    }

    Iterator<Thread> tit = ts.iterator();
    int i = 0;

    while (tit.hasNext()) {
      Thread t = tit.next();
      try {

        t.join();
        System.out.println("done." + (++i));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("finish.");

    Thread.sleep(1000000l);


  }

  public static class MutiThread implements Runnable {

    private String name;

    private JedisCluster jc;

    private int size;

    public MutiThread(String name, JedisCluster jc, int size) {
      this.name = name;
      this.jc = jc;
      this.size = size;
    }

    @Override
    public void run() {
      for (int i = 0; i < size; i++) {
        jc.set(name + i, System.currentTimeMillis() + "");
      }
    }
  }


  @Test
  public void testSub() {

    JedisCluster jc = inst.getJedisCluster();

    String k = "lock:1234";
    String v = "123";

    String code = jc.set(k, v, "NX", "EX", 86400);
    System.out.println(code);
  }

  @Test
  public void testPub() {

  }

  @Test
  public void testSmembers() {
    JedisCluster jc = inst.getJedisCluster();

    Set<String> set = jc.smembers("a");

    System.out.println(set);

    jc.sadd("a", "a");

    set = jc.smembers("a");

    System.out.println(set);

    jc.srem("a", "a");

    set = jc.smembers("a");
    System.out.println(set);


  }

}
