package win.sinno.redis.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import win.sinno.common.util.IdWorker;
import win.sinno.common.util.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分布式集群工具
 *
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2017-05-26 11:32.
 */
public class ClusterUtil {

    public static final Logger LOG = LoggerFactory.getLogger(ClusterUtil.class);
    //    private static Map<String, Integer> runMap = new HashMap<String, Integer>();
    public static final String PRE_KEY = "distid:";

    /**
     * @param redisson
     * @param hostName
     * @param clusterName
     * @param maxClusterCount
     * @return
     */
    public static Integer getWorkId(final RedissonClient redisson, String hostName, final String clusterName, int maxClusterCount) {

        Validate.notNull(redisson, "redisson is not null!");
        Validate.notBlank(hostName, "hostName can not be blank!");
        Validate.notBlank(clusterName, "clusterName can not be blank!");
        Validate.isTrue(maxClusterCount > 0, "maxClusterCount should > 0");

//        String hostKey = clusterName + ":" + hostName;
//        if (runMap.containsKey(hostKey)) {
//            Integer id = runMap.get(hostKey);
//            if (id != null && id > 0) {
//                return id;
//            }
//        }

        RLock idworkLock = redisson.getLock(PRE_KEY + clusterName);
        idworkLock.lock();

        Integer id = null;
        boolean getIdFlag = false;

//        while (!getIdFlag) {
        RMap<Integer, String> idworkIdRef = redisson.getMap(PRE_KEY + clusterName + "_id_ref");

        String runname = null;
        int tryCount = 0;

        List<Integer> exIdList = new ArrayList<Integer>();


        while (!getIdFlag && tryCount < maxClusterCount) {

            id = NumberUtils.nextInt(1, maxClusterCount, exIdList);
            if (id != null) {
                runname = idworkIdRef.get(id);

                if (StringUtils.isEmpty(runname)) {
                    getIdFlag = true;

                } else {
                    //check id
                    RAtomicLong atomicLong = redisson.getAtomicLong(PRE_KEY + clusterName + "_" + id);
                    long ts = atomicLong.get();
                    if (System.currentTimeMillis() - ts > 1800000) {
                        getIdFlag = true;
                    } else {
                        exIdList.add(id);
                        id = 0;
                    }
                }

                if (getIdFlag) {
                    final int workId = id;
                    LOG.info("clusterName:{} , hostName:{} can not get work id , after sleep 20s try again!", new Object[]{clusterName, hostName});
                    idworkIdRef.put(id, hostName);
                    RAtomicLong atomicLong = redisson.getAtomicLong(PRE_KEY + clusterName + "_" + id);
                    atomicLong.set(System.currentTimeMillis());
                    atomicLong.expire(1, TimeUnit.HOURS);
                    Thread refreshThread = new Thread() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    RAtomicLong atomicLong = redisson.getAtomicLong(PRE_KEY + clusterName + "_" + workId);
                                    atomicLong.set(System.currentTimeMillis());
                                    atomicLong.expire(1, TimeUnit.HOURS);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    Thread.sleep(600000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    refreshThread.setDaemon(true);
                    refreshThread.setName("idworkRefreshThread[" + clusterName + "]");
                    refreshThread.start();
                }

                tryCount++;
            }
        }

//            if (id == 0) {
//                try {
//                    LOG.info("clusterName:{} , hostName:{} can not get work id , after sleep 20s try again!", new Object[]{clusterName, hostName});
//                    Thread.sleep(20000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

//        workId = id;
//        runMap.put(hostKey, workId);

        idworkLock.unlock();
        return id;
    }


    public static IdWorker getIdWorker(final RedissonClient redisson, String hostName, final String clusterName, int maxClusterCount) {
        Integer workId = getWorkId(redisson, hostName, clusterName, maxClusterCount);
        if (workId != null && workId > 0) {
            IdWorker idWorker = new IdWorker(workId);
            return idWorker;
        }
        return null;
    }
}
