package win.sinno.redis;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2017-05-25 16:22.
 */
public class RedissonTest {
    private Config config;
    private RedissonClient redissonClient;

    {
        config = new Config();
        config.useSingleServer()
                .setClientName("sonTest")
                .setAddress("10.1.1.131:6379")
                .setPassword("redis")
                .setDatabase(3)
                .setConnectTimeout(5000)
                .setConnectionPoolSize(16)
                .setConnectionMinimumIdleSize(8);

        redissonClient = Redisson.create(config);
    }

    @Test
    public void startTest() {
        RedissonClient redissonClient = Redisson.create(config);

        RAtomicLong rAtomicLong = redissonClient.getAtomicLong("myLong");
        rAtomicLong.set(3);
        rAtomicLong.compareAndSet(3, 401);
    }

    @Test
    public void testBucket() {
        RedissonClient redissonClient = Redisson.create(config);

        RBucket<String> bucket = redissonClient.getBucket("hello");
        String h = bucket.get();
        System.out.println("get hello:" + h);
        bucket.set("world", 60, TimeUnit.SECONDS);

        h = bucket.get();
        System.out.println("get hello:" + h);
    }

    @Test
    public void testBucketObject() {
        RBucket<Person> bucket = redissonClient.getBucket("person");

        Person h = bucket.get();
        System.out.println("get hello:" + h);
        if (h == null) {
            h = new Person();
            h.name = "小明";
            h.age = 43;
            bucket.set(h, 60, TimeUnit.SECONDS);
        }

        h = bucket.get();
        System.out.println("get person:" + h);
    }

    @Test
    public void testTll() {
        RKeys keys = redissonClient.getKeys();

//        redissonClient.
    }

    @Test
    public void testList() {
        RList<Person> personRList = redissonClient.getList("persons");
        System.out.println(personRList.size());
        personRList.add(new Person("小bai", 18));
        personRList.add(new Person("小红", 12));
        personRList.add(new Person("小黑", 16));

        System.out.println(personRList.size());

        RList<Person> p1_2 = personRList.subList(1, 3);
        System.out.println(p1_2.readAll());
    }

    @Test
    public void testListRead() {
        RList<Person> personRList = redissonClient.getList("persons");
        System.out.println(personRList.size());

        RList<Person> p1_2 = personRList.subList(1, 3);
        System.out.println(p1_2.readAll());
    }

    @Test
    public void testListDel() {
        RList<Person> personRList = redissonClient.getList("persons");
        System.out.println(personRList.remove(1));
    }

    @Test
    public void testListClear() {
        RList<Person> personRList = redissonClient.getList("persons");
        personRList.clear();
    }

    @Test
    public void testLock1() throws InterruptedException {
        RLock lock = redissonClient.getLock("lock");
        lock.lock();
        System.out.println("lock1");

        Thread.sleep(10000l);
    }


    @Test
    public void testLock2() throws InterruptedException {
        RLock lock = redissonClient.getLock("lock");
        lock.lock();
        System.out.println("lock2");

        Thread.sleep(10000l);
    }

    @Test
    public void testBitSet() {
        RBitSet bitSet = redissonClient.getBitSet("bitSet");
        bitSet.set(11, true);
        bitSet.set(121, true);
        System.out.println(bitSet.get(11));
        System.out.println(bitSet.get(121));
        System.out.println(bitSet.get(12));
    }

    @Test
    public void testBitSet2() {
        RBitSet bitSet = redissonClient.getBitSet("bitSet22");
        bitSet.set(11, true);
        bitSet.set(121, true);
        System.out.println(bitSet.get(11));
        System.out.println(bitSet.get(121));
        System.out.println(bitSet.get(12));
    }

    static class Person {
        private String name;
        private int age;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }


}
