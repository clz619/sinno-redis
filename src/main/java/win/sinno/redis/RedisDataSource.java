package win.sinno.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import win.sinno.redis.constant.LoggerConfigs;
import win.sinno.redis.exception.MissAnnotationException;
import win.sinno.redis.interfaces.IRedisCommand;
import win.sinno.redis.model.KeyMapPair;
import win.sinno.redis.model.KeyValuePair;
import win.sinno.redis.model.KeyValueWithScorePair;
import win.sinno.redis.parse.HashMapParser;
import win.sinno.redis.parse.ListParser;
import win.sinno.redis.parse.SetParser;
import win.sinno.redis.parse.SortedSetParser;

/**
 * redis manager
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/6/23 下午12:00
 */
public class RedisDataSource implements IRedisCommand {

  private JedisPoolConfig jedisPoolConfig;

  private String host;

  private int port;

  private int timeout;

  private String password;

  private int database;

  private JedisPool pool;

  public JedisPoolConfig getJedisPoolConfig() {
    return jedisPoolConfig;
  }

  public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
    this.jedisPoolConfig = jedisPoolConfig;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getDatabase() {
    return database;
  }

  public void setDatabase(int database) {
    this.database = database;
  }

  public JedisPool getPool() {
    return pool;
  }

  public void setPool(JedisPool pool) {
    this.pool = pool;
  }

  public RedisDataSource() {
  }

  public RedisDataSource(JedisPoolConfig jedisPoolConfig, String host, int port, int timeout,
      String password, Integer database) {
    // redis manager
    pool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
  }

  public void init() {
    pool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
  }

  /**
   * redis close
   */
  public void close() {
    if (pool != null && !pool.isClosed()) {
      pool.close();
    }
  }


  public Jedis getJedis() {
    if (pool == null) {
      init();
    }
    return pool.getResource();
  }

  /**
   * 获取数据
   */
  public String get(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String val = jedis.get(key);
      LoggerConfigs.REDIS_LOG.debug("get:{} value:{}", new Object[]{key, val});
      return val;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  @Override
  public byte[] get(byte[] key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      byte[] val = jedis.get(key);
      LoggerConfigs.REDIS_LOG.debug("get:{} value:{}", new Object[]{key, val});
      return val;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 设置key值
   */
  public String set(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.set(key, value);
      LoggerConfigs.REDIS_LOG.debug("get:{} value:{} ret:{}", new Object[]{key, value, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  @Override
  public Long ttl(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.ttl(key);
      LoggerConfigs.REDIS_LOG.debug("ttl:{} ret:{}", new Object[]{key, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 将key设置值为value，如果key不存在，这种情况下等同SET命令。 当key存在时，什么也不做。SETNX是”SET if Not eXists”的简写。
   * <p>
   * 返回
   * 1 如果key被设置了
   * 0 如果key没有被设置
   */
  public Long setnx(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.setnx(key, value);
      LoggerConfigs.REDIS_LOG.debug("setnx:{} value:{} ret:{}", new Object[]{key, value, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 设置key对应字符串value，并且设置key在给定的seconds时间之后超时过期。
   */
  public String setex(String key, int seconds, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.setex(key, seconds, value);
      LoggerConfigs.REDIS_LOG
          .debug("setex:{} seconds:{} value:{} ret:{}", new Object[]{key, seconds, value, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public String setnxpx(String key, String value, long milliseconds) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.set(key, value, "NX", "PX", milliseconds);
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public String setnxex(String key, String value, long seconds) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.set(key, value, "NX", "EX", seconds);
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public String setxxpx(String key, String value, long milliseconds) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.set(key, value, "XX", "PX", milliseconds);
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public String setxxex(String key, String value, long seconds) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.set(key, value, "XX", "EX", seconds);
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public Long del(String... keys) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.del(keys);
      LoggerConfigs.REDIS_LOG.debug("del:{} ret:{}", new Object[]{ArrayUtils.toString(keys), ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 过期
   */
  public Long expire(String key, int seconds) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.expire(key, seconds);
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * @param key
   * @param unixTime
   * @return
   */
  public Long expireAt(String key, long unixTime) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.expireAt(key, unixTime);
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 查询keys
   */

  public Set<String> keys(String pattern) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Set<String> keys = jedis.keys(pattern);
      return keys;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 整数原子减1
   */

  public Long decr(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long val = jedis.decr(key);
      return val;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 整数原子减decrement
   */

  public Long decrBy(String key, Long decrement) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long val = jedis.decrBy(key, decrement);
      return val;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 执行原子加1操作
   */

  public Long incr(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long val = jedis.incr(key);
      return val;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 执行原子增加一个整数
   */

  public Long incrBy(String key, int increment) {

    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long val = jedis.incrBy(key, increment);
      return val;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }

  }

  //-------列表处理-------

  //-------列表处理-------

  /**
   * 列表-在key列表左侧插入元素values
   */
  public Long lpush(String key, String... values) {
    Jedis jedis = null;
    try {
      jedis = getJedis();

      Long ret = jedis.lpush(key, values);
      LoggerConfigs.REDIS_LOG
          .debug("get:{} value:{},ret:{}", new Object[]{key, ArrayUtils.toString(values), ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 列表-在obj的key列表左侧插入obj的元素values
   */
  public <T> Long lpush(T obj) throws MissAnnotationException {
    Jedis jedis = null;

    try {
      KeyValuePair keyValuePair = ListParser.obj2Set(obj);

      jedis = getJedis();

      return jedis.lpush(keyValuePair.getKey(), keyValuePair.getValue());

    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }

  }

  /**
   * 列表-key列表从左侧取出一个值
   */
  public String lpop(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.lpop(key);
      LoggerConfigs.REDIS_LOG.debug("lpop:{} val:{}", new Object[]{key, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 列表-key列表从左侧取出一个值，并转换为T
   */
  public <T> T lpop(T obj, String key) {
    Jedis jedis = null;

    try {
      jedis = getJedis();
      String value = jedis.lpop(key);

      if (StringUtils.isEmpty(value)) {
        return null;
      }

      ListParser.string2Obj(obj, value);
      LoggerConfigs.REDIS_LOG.debug("lpop:{} val:{}", new Object[]{key, obj});
      return obj;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 列表-在key列表右侧插入元素values
   */
  public Long rpush(String key, String... values) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.rpush(key, values);
      LoggerConfigs.REDIS_LOG
          .debug("rpush:{} val:{} ret:{}", new Object[]{key, ArrayUtils.toString(values), ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 列表-key列表从右侧取出一个值
   */
  public <T> Long rpush(T obj) throws MissAnnotationException {
    Jedis jedis = null;

    try {
      KeyValuePair keyValuePair = ListParser.obj2Set(obj);

      jedis = getJedis();

      return jedis.rpush(keyValuePair.getKey(), keyValuePair.getValue());

    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 列表-key列表重最右边取出一个数
   */
  public String rpop(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.rpop(key);
      LoggerConfigs.REDIS_LOG.debug("rpop:{} ret:{}", new Object[]{key, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 列表-key从右侧取出一个值，并转换为T
   */
  public <T> T rpop(T obj, String key) {
    Jedis jedis = null;

    try {
      jedis = getJedis();
      String value = jedis.rpop(key);

      if (StringUtils.isEmpty(value)) {
        return null;
      }

      ListParser.string2Obj(obj, value);
      LoggerConfigs.REDIS_LOG.debug("lpop:{} val:{}", new Object[]{key, obj});
      return obj;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }


  /**
   * 列表-获取列表key区间start-end的值
   * start从0开始
   * end=-1,表示取出所有值,end值大于列表值不会出现越界错误,获取所有值
   */
  public List<String> lrange(String key, long start, long end) {
    Jedis jedis = null;
    try {
      jedis = getJedis();

      List<String> ret = jedis.lrange(key, start, end);

      LoggerConfigs.REDIS_LOG
          .debug("lrange:{} start:{} end:{} ret:{}", new Object[]{key, start, end, ret});

      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 列表-获取列表key区间start-end的值
   * start从0开始
   * end=-1,表示取出所有值
   */
  public <T> List<T> lrange(T t, String key, long start, long end) {
    Jedis jedis = null;
    try {
      jedis = getJedis();

      List<String> valueList = jedis.lrange(key, start, end);

      if (CollectionUtils.isNotEmpty(valueList)) {
        return Collections.emptyList();
      }

      List<T> ret = ListParser.string2Obj(t, valueList);

      LoggerConfigs.REDIS_LOG
          .debug("lrange:{} start:{} end:{} ret:{}", new Object[]{key, start, end, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 列表-获取列表key的长度
   */
  public Long llen(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();

      Long ret = jedis.llen(key);

      LoggerConfigs.REDIS_LOG.debug("llen:{} ret:{}", new Object[]{key, ret});

      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 列表-从列表key中获取index的值
   */
  public String lindex(String key, long index) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.lindex(key, index);
      LoggerConfigs.REDIS_LOG.debug("lindex:{} index:{} ret:{}", new Object[]{key, index, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 列表-删除列表key区间value的值
   * count>0时:从表头开始向表尾搜索,移除与value值相等的元素,数量为value
   * count<0时:从表尾开始向表头搜索,移除与value值相等的元素,数量为value
   * count=0时:移除表中所有与value相等的值
   */
  public Long lrem(String key, long count, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.lrem(key, count, value);
      LoggerConfigs.REDIS_LOG
          .debug("lrem key:{} count:{} value:{} ret:{}", new Object[]{key, count, value, ret});

      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 修剪列表
   * Ltrim 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
   * 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
   */
  public String ltrim(String key, long start, long stop) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.ltrim(key, start, stop);

      LoggerConfigs.REDIS_LOG
          .debug("ltrim key:{} start:{} stop:{} ret:{}", new Object[]{key, start, stop, ret});

      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  //-------集合处理-------

  /**
   * 集合-加入数据至集合
   */
  public Long sadd(String key, String... members) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.sadd(key, members);

      LoggerConfigs.REDIS_LOG
          .debug("sadd:{} members:{} ret:{}", new Object[]{key, ArrayUtils.toString(members), ret});

      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }

  }

  /**
   * 集合添加
   */
  public <T> Long sadd(T t) throws MissAnnotationException {

    Jedis jedis = null;
    try {
      jedis = getJedis();

      KeyValuePair keyValuePair = SetParser.obj2Set(t);
      Long ret = jedis.sadd(keyValuePair.getKey(), keyValuePair.getValue());

      LoggerConfigs.REDIS_LOG.debug("sadd obj:{} ret:{}", new Object[]{t, ret});

      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 集合-获取集合的成员数
   */
  public Long scard(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.scard(key);

      LoggerConfigs.REDIS_LOG.debug("scard key:{} ret:{}", new Object[]{key, ret});

      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }

  }

  /**
   * 集合-列出集合的元素
   */
  public Set<String> smembers(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Set<String> ret = jedis.smembers(key);

//            if (CollectionUtils.isNotEmpty(ret)) {
//                if (ret.size() < 5) {
//                    LoggerConfigs.REDIS_LOG.info("smembers:{} ret:{}", new Object[]{key, ret});
//                } else {
//                    LoggerConfigs.REDIS_LOG.info("smembers:{} ret:size>5...", key);
//                }
//            }
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 集合-列出集合的元素
   */
  public <T> Set<T> smembers(T t, String key) {

    Jedis jedis = null;
    try {
      jedis = getJedis();

      Set<String> valSet = jedis.smembers(key);

      Set<T> ret = SetParser.string2Obj(t, valSet);

      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 集合-判断member是否是集合key的成员
   */
  public boolean sismember(String key, String member) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      boolean ret = jedis.sismember(key, member);
//            LoggerConfigs.REDIS_LOG.info("sismember:{} member:{} ret:{}", new Object[]{key, member, ret});

      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 集合-返回给定所有集合的差集
   * 以第一个集合为参照物
   */
  public Set<String> sdiff(String... keys) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.sdiff(keys);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 集合-返回给定所有集合的交集
   */
  public Set<String> sinter(String... keys) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.sinter(keys);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 集合-返回给定所有集合的并集
   */
  public Set<String> sunion(String... keys) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.sunion(keys);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 集合-移除并返回key集合的的一个随机元素
   */
  public String spop(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.spop(key);
//            LoggerConfigs.REDIS_LOG.info("spop:{} ret:{}", new Object[]{key, ret});

      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 集合-移除并返回key集合的的count个随机元素
   */
  public Set<String> spop(String key, long count) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.spop(key, count);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 集合-返回集合中一个随机元素
   */
  public String srandmember(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.srandmember(key);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 集合-返回集合中count个随机元素
   */
  public List<String> srandmember(String key, int count) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      List<String> ret = jedis.srandmember(key, count);
//            LoggerConfigs.REDIS_LOG.info("srandmember:{} ret:{}", new Object[]{key, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 集合-移除key集合指定元素
   */
  public Long srem(String key, String... members) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.srem(key, members);
//            LoggerConfigs.REDIS_LOG.info("srem:{} members:{} ret:{}", new Object[]{key, ArrayUtils.toString(members), ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  //-------有序集合处理-------

  public Long zadd(String key, double score, String member) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.zadd(key, score, member);
//            LoggerConfigs.REDIS_LOG.info("zadd:{} score:{} member:{} ret:{}", new Object[]{key, score, member, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
   */

  public <T> Long zadd(T t) throws MissAnnotationException {
    Jedis jedis = null;
    try {
      jedis = getJedis();

      KeyValueWithScorePair keyValueWithScorePair = SortedSetParser.obj2Set(t);

      Long ret = jedis.zadd(keyValueWithScorePair.getKey(), keyValueWithScorePair.getScore(),
          keyValueWithScorePair.getValue());
//            LoggerConfigs.REDIS_LOG.info("zadd:{} score:{} member:{} ret:{}", new Object[]{key, score, member, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 获取有序集合的成员数
   */

  public Long zcard(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.zcard(key);
//            LoggerConfigs.REDIS_LOG.info("zcard:{} ret:{}", new Object[]{key, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 通过索引区间返回有序集合成指定区间内的成员
   */

  public List<String> zrange(String key, Long start, Long stop) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Set<String> retSet = jedis.zrange(key, start, stop);

      List<String> ret = new ArrayList<String>();

      ret.addAll(retSet);
//            LoggerConfigs.REDIS_LOG.info("zcard:{} ret:{}", new Object[]{key, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 获取有序集合的所有成员
   */

  public List<String> zrangeAll(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long size = jedis.scard(key);

      Set<String> retSet = null;

      List<String> ret = null;
      if (size > 0) {
        retSet = jedis.zrange(key, 0, size - 1);
        ret.addAll(retSet);
      }
//            LoggerConfigs.REDIS_LOG.info("zcard:{} ret:{}", new Object[]{key, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 获取有序集合的所有成员-转换为对象
   */

  public <T> List<T> zrangeAll4Obj(T obj, String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long size = jedis.zcard(key);

      Set<String> valSet = null;

      if (size > 0) {
        valSet = jedis.zrange(key, 0, size - 1);
      }
//            LoggerConfigs.REDIS_LOG.info("zcard:{} ret:{}", new Object[]{key, ret});

      List<T> ret = null;
      if (CollectionUtils.isNotEmpty(valSet)) {
        ret = SortedSetParser.string2Obj(obj, valSet);
      }

      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  //-------哈希处理-------

  /**
   * 哈希-单属性设置哈希key的值
   */
  public Long hset(String key, String field, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.hset(key, field, value);
//            LoggerConfigs.REDIS_LOG.info("hset:{} field:{} value:{} ret:{}", new Object[]{key, field, value, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }


  /**
   * 哈希-获取哈希key的field的值
   */
  public String hget(String key, String field) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.hget(key, field);
//            LoggerConfigs.REDIS_LOG.info("hget:{} field:{} ret:{}", new Object[]{key, field, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 哈希-获取key的所有哈希值
   */
  public Map<String, String> hgetAll(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Map<String, String> ret = jedis.hgetAll(key);
//            LoggerConfigs.REDIS_LOG.info("hgetAll:{} ret:{}", new Object[]{key, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 根据key和对象初始值,获取对象转换
   */
  public <T> T hgetAll(T t, String key) {
    Map<String, String> hash = hgetAll(key);
    T ret = HashMapParser.hash2Obj(t, hash);
//        LoggerConfigs.REDIS_LOG.info("hgetAll:{} ret:{}", new Object[]{key, ret});
    return ret;
  }

  /**
   * 哈希-获取哈希表key的所有字段
   */
  public Set<String> hkeys(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.hkeys(key);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 哈希-获取哈希表key的所有值
   */
  public List<String> hvals(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.hvals(key);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 哈希-获取哈希表中字段的数量
   */
  public Long hlen(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.hlen(key);
//            LoggerConfigs.REDIS_LOG.info("hlen:{} ret:{}", new Object[]{key, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 哈希-获取哈希表key给定字段的值
   */
  public List<String> hmget(String key, String... fields) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      return jedis.hmget(key, fields);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 哈希-获取key,指定属性并返回对象
   */
  public <T> T hmget(T t, String key, String... fields) {
    List<String> rets = hmget(key, fields);

    Map<String, String> hash = new HashMap<String, String>();

    int i = 0;
    for (String field : fields) {
      hash.put(field, rets.get(i++));
    }

    T ret = HashMapParser.hash2Obj(t, hash);

//        LoggerConfigs.REDIS_LOG.info("hmget:{} fields:{} ret:{}", new Object[]{key, ArrayUtils.toString(fields), t});

    return ret;
  }

  /**
   * 哈希-多属性设置哈希的值
   * key为KeyMapPair-key
   * hash为KeyMapPair-map
   */
  public String hmset(KeyMapPair keyMapPair) {
    return hmset(keyMapPair.getKey(), keyMapPair.getMap());
  }

  /**
   * 哈希-设置hash对象值hash表中
   */
  public <T> String hmset(T t) throws MissAnnotationException {
    KeyMapPair keyMapPair = HashMapParser.obj2Hash(t);
    return hmset(keyMapPair);
  }

  /**
   * 缓存操作
   */

  public <T> String hmsetAndExpire(T t, int seconds) throws MissAnnotationException {

    //设置进redis
    KeyMapPair keyMapPair = HashMapParser.obj2Hash(t);
    String ret = hmset(keyMapPair);

    //设置过期时间
    String key = keyMapPair.getKey();
    expire(key, seconds);
    return ret;
  }


  /**
   * 哈希-多属性设置哈希key的值
   */
  public String hmset(String key, Map<String, String> hash) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String ret = jedis.hmset(key, hash);
//            LoggerConfigs.REDIS_LOG.info("hmset:{} hash:{} ret:{}", new Object[]{key, hash, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 哈希-只有在字段field不存在时,设置哈希表字段的值
   */
  public Long hsetnx(String key, String field, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.hsetnx(key, field, value);
//            LoggerConfigs.REDIS_LOG.info("hsetnx:{} field:{} value:{} ret:{}", new Object[]{key, field, value, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 哈希-为哈希表key指定的field字段加上增加值value
   */
  public Long hincrBy(String key, String field, long value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.hincrBy(key, field, value);
//            LoggerConfigs.REDIS_LOG.info("hincrBy:{} field:{} value:{} ret:{}", new Object[]{key, field, value, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }


  /**
   * 哈希-查询哈希表key中,指定的field是否存在
   */
  public Boolean hexists(String key, String field) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Boolean ret = jedis.hexists(key, field);
//            LoggerConfigs.REDIS_LOG.info("hexists:{} field:{} ret:{}", new Object[]{key, field, ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  /**
   * 哈希表-删除一个或多个哈希表key的字段
   */
  public Long hdel(String key, String... fields) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      Long ret = jedis.hdel(key, fields);
//            LoggerConfigs.REDIS_LOG.info("hdel:{} fields:{} ret:{}", new Object[]{key, ArrayUtils.toString(fields), ret});
      return ret;
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }


}
