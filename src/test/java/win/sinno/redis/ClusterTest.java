package win.sinno.redis;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import win.sinno.common.util.IdWorker;
import win.sinno.common.util.NumberUtils;
import win.sinno.common.util.RuntimeUtil;
import win.sinno.redis.util.ClusterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2017-05-26 09:39.
 */
public class ClusterTest {
    private Config config;
    private RedissonClient redisson;

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

        redisson = Redisson.create(config);
    }

    @Test
    public void testId() throws Exception {

        int maxWorkId = 3;

        String hostRunname = RuntimeUtil.getRunName();

        RLock idworkLock = redisson.getLock("idwork");
        idworkLock.lock();

        int id = 0;
        boolean getIdFlag = false;
        while (!getIdFlag) {
            RMap<Integer, String> idworkIdRef = redisson.getMap("idwork-idref");

            String runname = null;

            int tryCount = 0;

            List<Integer> exIdList = new ArrayList<Integer>();

            while (!getIdFlag && tryCount < maxWorkId) {

                id = NumberUtils.nextInt(1, 3, exIdList);
                runname = idworkIdRef.get(id);

                if (StringUtils.isEmpty(runname)) {
                    getIdFlag = true;


                } else {
                    //check id
                    RAtomicLong atomicLong = redisson.getAtomicLong("idwork-" + id);
                    long ts = atomicLong.get();
                    if (System.currentTimeMillis() - ts > 60000) {
                        getIdFlag = true;
                    } else {
                        exIdList.add(id);
                        id = 0;
                    }
                }

                if (getIdFlag) {
                    final int workId = id;

                    idworkIdRef.put(id, hostRunname);
                    RAtomicLong atomicLong = redisson.getAtomicLong("idwork-" + id);
                    atomicLong.set(System.currentTimeMillis());
                    atomicLong.expire(1, TimeUnit.DAYS);
                    Thread refreshThread = new Thread() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    RAtomicLong atomicLong = redisson.getAtomicLong("idwork-" + workId);
                                    atomicLong.set(System.currentTimeMillis());
                                    atomicLong.expire(1, TimeUnit.DAYS);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    Thread.sleep(30000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    refreshThread.setDaemon(true);
                    refreshThread.setName("idwork-refresh");
                    refreshThread.start();
                }

                tryCount++;
            }

            if (id == 0) {
                System.out.println("not get id,reget，after sleep 1min");
                Thread.sleep(60000);
            } else {
                System.out.println(Thread.currentThread().getName() + " runname : " + hostRunname
                        + " get id : " + id);
            }
        }

        idworkLock.unlock();

        Thread.sleep(10000000);
    }

    @Test
    public void testDistIdErrParam() {
//        DistributedIdUtil.getWorkId(null, null, null, -1);
//        DistributedIdUtil.getWorkId(redisson, null, null, -1);
//        ClusterUtil.getWorkId(redisson, RuntimeUtil.getRunName(), null, -1);
        long b = System.currentTimeMillis();
        int workId = ClusterUtil.getWorkId(redisson, RuntimeUtil.getRunName(), "sinno", 103);
        System.out.println(workId);
        long e = System.currentTimeMillis();
        System.out.println("useTs:" + (e - b) + "ms");
    }

    @Test
    public void testDistId() throws InterruptedException {

        for (int i = 0; i < 110; i++) {
            final String name = "work[" + i + "]";
            Thread thread = new Thread() {
                @Override
                public void run() {
                    int i = ClusterUtil.getWorkId(redisson, name, "sinno", 100);
                    System.out.println(Thread.currentThread().getName() + " get id:" + i);
                }
            };
            thread.start();
        }

        Thread.sleep(10000000l);
    }

    @Test
    public void testIdWorker() {
        IdWorker idWorker = ClusterUtil.getIdWorker(redisson, RuntimeUtil.getRunName(), "sinno", 1000);

        if (idWorker != null) {
            for (int i = 1; i < 10; i++) {
                System.out.println(idWorker.nextId());
            }
        }
    }


}
