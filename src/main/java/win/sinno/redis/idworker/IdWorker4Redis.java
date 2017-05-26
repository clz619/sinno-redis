package win.sinno.redis.idworker;

import org.apache.commons.lang3.Validate;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import win.sinno.common.util.IdWorker;
import win.sinno.redis.util.ClusterUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * id worker 4 redis
 *
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2017-05-26 17:24.
 */
public class IdWorker4Redis {

    private static final Logger LOG = LoggerFactory.getLogger(IdWorker4Redis.class);

    private AtomicBoolean initFlag = new AtomicBoolean(false);
    private Config config;
    private RedissonClient redisson;
    private IdWorker idWorker;

    private String clusterName;
    private String hostName;
    private String redisHost;
    private int port;
    private String password;
    private int timeout;
    private int database;
    private int connectionPoolSize = 16;
    private int connectionMinimumIdleSize = 2;

    private IdWorker4Redis(IdWorker4RedisConfig config) {
        this.clusterName = config.getClusterName();
        this.hostName = config.getHostName();
        this.redisHost = config.getRedisHost();
        this.port = config.getRedisPort();
        this.password = config.getRedisPassword();
        this.timeout = config.getRedisTimeout();
        this.database = config.getRedisDatabase();

        Validate.notBlank(clusterName, "clusterName can not empty");
        Validate.notBlank(hostName, "hostName can not empty");
        Validate.notBlank(redisHost, "redisHost can not empty");

        init();
    }


    public static IdWorker4Redis newInstance(IdWorker4RedisConfig config) {
        return new IdWorker4Redis(config);
    }

    public void init() {
        if (initFlag.compareAndSet(false, true)) {
            String address = redisHost + ":" + port;

            config = new Config();
            config.useSingleServer().setClientName("idworker:" + clusterName + ":" + hostName).setAddress(address).setPassword(password)
                    .setDatabase(database).setConnectTimeout(timeout).setConnectionPoolSize(connectionPoolSize)
                    .setConnectionMinimumIdleSize(connectionMinimumIdleSize);

            redisson = Redisson.create(config);

            idWorker = ClusterUtil.getIdWorker(redisson, hostName, clusterName,
                    (int) IdWorker.maxWorkerId);

            if (idWorker == null) {
                initFlag.compareAndSet(true, false);
            }
        }
    }

    public long nextId() {
        long ret = 0;
        try {
            ret = idWorker.nextId();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return ret;
    }

}
