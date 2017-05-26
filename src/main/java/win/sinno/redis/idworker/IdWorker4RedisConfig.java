package win.sinno.redis.idworker;

/**
 * id worker config
 *
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2017-05-26 17:37.
 */
public class IdWorker4RedisConfig {

    private String clusterName;
    private String hostName;
    private String redisHost;
    private int redisPort = 6379;
    private String redisPassword;
    private int redisTimeout = 20 * 1000;
    private int redisDatabase = 0;

    public String getClusterName() {
        return clusterName;
    }

    public IdWorker4RedisConfig setClusterName(String clusterName) {
        this.clusterName = clusterName;
        return this;
    }

    public String getHostName() {
        return hostName;
    }

    public IdWorker4RedisConfig setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public IdWorker4RedisConfig setRedisHost(String redisHost) {
        this.redisHost = redisHost;
        return this;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public IdWorker4RedisConfig setRedisPort(int redisPort) {
        this.redisPort = redisPort;
        return this;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public IdWorker4RedisConfig setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
        return this;
    }

    public int getRedisTimeout() {
        return redisTimeout;
    }

    public IdWorker4RedisConfig setRedisTimeout(int redisTimeout) {
        this.redisTimeout = redisTimeout;
        return this;
    }

    public int getRedisDatabase() {
        return redisDatabase;
    }

    public IdWorker4RedisConfig setRedisDatabase(int redisDatabase) {
        this.redisDatabase = redisDatabase;
        return this;
    }
}
