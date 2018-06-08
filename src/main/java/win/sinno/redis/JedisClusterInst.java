package win.sinno.redis;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * com.taovip.cached.redis.JedisClusterInst
 *
 * @author chenlizhong@qipeng.com
 * @date 2018/4/24
 */
public class JedisClusterInst {

  // host1:port1,host2:port2
  private String hostAndPorts;

  private String password;

  private int connectionTimeout = 2000;

  private int soTimeout = 2000;

  private int maxAttempts = 5;

  private int maxTotal = 8;

  private int maxIdle = 8;

  private int minIdle = 0;

  private JedisCluster jedisCluster;

  public void init() {
    if (StringUtils.isBlank(hostAndPorts)) {
      throw new InvalidParameterException("hostAndPorts can't blank!");
    }

    String[] arrs = hostAndPorts.split(",");

    Set<HostAndPort> hostAndPortSet = new HashSet<>();

    for (String arr : arrs) {
      String[] hp = arr.split(":");
      if (hp.length != 2) {
        continue;
      }
      try {
        String host = hp[0];
        int port = Integer.valueOf(hp[1]);
        HostAndPort hostAndPort = new HostAndPort(host, port);
        hostAndPortSet.add(hostAndPort);
      } catch (Exception e) {
        throw new InvalidParameterException("hostAndPorts is err!");
      }
    }

    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    if (maxTotal > 0) {
      poolConfig.setMaxTotal(maxTotal);
    }
    if (maxIdle > 0) {
      poolConfig.setMaxIdle(maxIdle);
    }
    if (minIdle > 0) {
      poolConfig.setMinIdle(minIdle);
    }

    if (StringUtils.isBlank(password)) {
      jedisCluster = new JedisCluster(hostAndPortSet, connectionTimeout, soTimeout, maxAttempts
          , poolConfig);
    } else {
      jedisCluster = new JedisCluster(hostAndPortSet, connectionTimeout, soTimeout, maxAttempts
          , password, poolConfig);
    }

  }

  public void close() throws IOException {
    if (jedisCluster != null) {
      jedisCluster.close();
    }
  }

  public String getHostAndPorts() {
    return hostAndPorts;
  }

  public void setHostAndPorts(String hostAndPorts) {
    this.hostAndPorts = hostAndPorts;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public int getSoTimeout() {
    return soTimeout;
  }

  public void setSoTimeout(int soTimeout) {
    this.soTimeout = soTimeout;
  }

  public int getMaxAttempts() {
    return maxAttempts;
  }

  public void setMaxAttempts(int maxAttempts) {
    this.maxAttempts = maxAttempts;
  }

  public int getMaxTotal() {
    return maxTotal;
  }

  public void setMaxTotal(int maxTotal) {
    this.maxTotal = maxTotal;
  }

  public int getMaxIdle() {
    return maxIdle;
  }

  public void setMaxIdle(int maxIdle) {
    this.maxIdle = maxIdle;
  }

  public int getMinIdle() {
    return minIdle;
  }

  public void setMinIdle(int minIdle) {
    this.minIdle = minIdle;
  }

  public JedisCluster getJedisCluster() {
    return jedisCluster;
  }

  public void setJedisCluster(JedisCluster jedisCluster) {
    this.jedisCluster = jedisCluster;
  }
}
