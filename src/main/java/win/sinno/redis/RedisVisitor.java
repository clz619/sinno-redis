package win.sinno.redis;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import win.sinno.redis.constant.LoggerConfigs;
import win.sinno.redis.exception.MissAnnotationException;
import win.sinno.redis.interfaces.IRedisCommand;
import win.sinno.redis.model.KeyMapPair;

import java.util.*;

/**
 * redis访问器
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/6/29 上午11:49
 */
public abstract class RedisVisitor implements IRedisCommand {

    // redis 管理器
    private RedisDataSource redisDataSource;

    /**
     * 关键字基础数据
     */
    private String baseKey;

    public RedisVisitor(String baseKey) {
        this.baseKey = baseKey;
    }

    public RedisDataSource getRedisDataSource() {
        return redisDataSource;
    }

    public void setRedisDataSource(RedisDataSource redisDataSource) {
        this.redisDataSource = redisDataSource;
    }

    public String genKey(String key) {
        if (StringUtils.isNotEmpty(key)) {
            return getBaseKey() + ":" + key;
        }
        return getBaseKey();
    }

    public String getBaseKey() {
        return baseKey;
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public String get(String key) {
        try {
            return redisDataSource.get(genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 设置key值
     *
     * @param key
     * @param value
     */
    public String set(String key, String value) {
        try {
            return redisDataSource.set(genKey(key), value);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * set key value
     * expire key seconds
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public String setex(String key, int seconds, String value) {
        try {
            return redisDataSource.setex(genKey(key), seconds, value);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public String setnxpx(String key, String value, long milliseconds) {
        try {
            return redisDataSource.setnxpx(genKey(key), value, milliseconds);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public String setnxex(String key, String value, long seconds) {
        try {
            return redisDataSource.setnxex(genKey(key), value, seconds);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public String setxxpx(String key, String value, long milliseconds) {
        try {
            return redisDataSource.setxxpx(genKey(key), value, milliseconds);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public String setxxex(String key, String value, long seconds) {
        try {
            return redisDataSource.setxxex(genKey(key), value, seconds);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 删除keys的对象
     *
     * @param keys
     * @return
     */

    public Long del(String... keys) {
        try {
            if (ArrayUtils.isEmpty(keys)) {
                return 0l;
            }

            Set<String> keySet = new HashSet<String>();
            for (String key : keys) {
                keySet.add(genKey(key));
            }
            return redisDataSource.del(keySet.toArray(new String[0]));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 设置key过期时间为seconds秒
     *
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(String key, int seconds) {
        try {
            redisDataSource.expire(genKey(key), seconds);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 设置key在unixTime时间过期
     *
     * @param key
     * @param unixTime
     * @return
     */
    public Long expireAt(String key, long unixTime) {
        try {
            redisDataSource.expireAt(genKey(key), unixTime);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询keys
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        try {
            return redisDataSource.keys(pattern);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }


    public Long decr(String key) {
        try {
            return redisDataSource.decr(genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }


    public Long decrBy(String key, Long decrement) {
        try {
            return redisDataSource.decrBy(this.genKey(key), decrement);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 原子增量加1
     *
     * @param key
     * @return
     */

    public Long incr(String key) {
        try {
            return redisDataSource.incr(this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 原子增量加increment
     *
     * @param key
     * @param increment
     * @return
     */

    public Long incrBy(String key, int increment) {
        try {
            return redisDataSource.incrBy(this.genKey(key), increment);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-在key列表左侧插入元素values
     *
     * @param key
     * @param values
     * @return
     */
    public Long lpush(String key, String... values) {
        try {
            return redisDataSource.lpush(this.genKey(key), values);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-在obj的key列表左侧插入obj的元素values
     *
     * @param obj
     * @return
     */
    public <T> Long lpush(T obj) throws MissAnnotationException {
        try {
            return redisDataSource.lpush(obj);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 列表-key列表从左侧取出一个值
     *
     * @param key
     * @return
     */

    public String lpop(String key) {
        try {
            return redisDataSource.lpop(this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-key列表从左侧取出一个值，并转换为T
     *
     * @param obj
     * @param key
     * @return
     */
    public <T> T lpop(T obj, String key) {
        try {
            return redisDataSource.lpop(obj, this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-在key列表右侧插入元素values
     *
     * @param key
     * @param values
     * @return
     */

    public Long rpush(String key, String... values) {
        try {
            return redisDataSource.rpush(this.genKey(key), values);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-key列表从右侧取出一个值
     *
     * @param obj
     * @return
     */
    public <T> Long rpush(T obj) throws MissAnnotationException {
        try {
            return redisDataSource.rpush(obj);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-key列表重最右边取出一个数
     *
     * @param key
     * @return
     */

    public String rpop(String key) {
        try {
            return redisDataSource.rpop(this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-key从右侧取出一个值，并转换为T
     *
     * @param obj
     * @param key
     * @return
     */
    public <T> T rpop(T obj, String key) {
        try {
            return redisDataSource.rpop(obj, this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-获取列表key区间start-end的值 start从0开始 end=-1,表示取出所有值,end值大于列表值不会出现越界错误,获取所有值
     *
     * @param key
     * @param start
     * @param end
     * @return
     */

    public List<String> lrange(String key, long start, long end) {
        try {
            return redisDataSource.lrange(this.genKey(key), start, end);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-获取列表key区间start-end的值
     * start从0开始
     * end=-1,表示取出所有值
     *
     * @param t
     * @param key
     * @param start
     * @param end
     * @return
     */
    public <T> List<T> lrange(T t, String key, long start, long end) {
        try {
            return redisDataSource.lrange(t, this.genKey(key), start, end);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-获取列表key的长度
     *
     * @param key
     * @return
     */

    public Long llen(String key) {
        try {
            return redisDataSource.llen(this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-从列表key中获取index的值
     *
     * @param key
     * @param index
     * @return
     */

    public String lindex(String key, long index) {
        try {
            return redisDataSource.lindex(this.genKey(key), index);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-删除列表key区间value的值
     * count>0时:从表头开始向表尾搜索,移除与value值相等的元素,数量为value
     * count<0时:从表尾开始向表头搜索,移除与value值相等的元素,数量为value count=0时:移除表中所有与value相等的值
     *
     * @param key
     * @param count
     * @param value
     * @return
     */

    public Long lrem(String key, long count, String value) {
        try {
            return redisDataSource.lrem(this.genKey(key), count, value);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public String ltrim(String key, long start, long stop) {
        try {
            return redisDataSource.ltrim(this.genKey(key), start, stop);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-加入数据至集合
     *
     * @param key
     * @param members
     * @return
     */

    public Long sadd(String key, String... members) {
        try {
            return redisDataSource.sadd(this.genKey(key), members);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-加入数据至集合
     *
     * @param t
     * @param <T>
     * @return
     * @throws MissAnnotationException
     */
    public <T> Long sadd(T t) {
        try {
            return redisDataSource.sadd(t);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-获取集合的成员数
     *
     * @param key
     * @return
     */

    public Long scard(String key) {
        try {
            return redisDataSource.scard(this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-列出集合的元素
     *
     * @param key
     * @return
     */

    public Set<String> smembers(String key) {
        try {
            return redisDataSource.smembers(this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-列出集合的元素
     *
     * @param t
     * @param key
     * @param <T>
     * @return
     */
    public <T> Set<T> smembers(T t, String key) {
        try {
            return redisDataSource.smembers(t, this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-判断member是否是集合key的成员
     *
     * @param key
     * @param member
     * @return
     */

    public boolean sismember(String key, String member) {
        try {
            return redisDataSource.sismember(this.genKey(key), member);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 集合-返回给定所有集合的差集 以第一个集合为参照物
     *
     * @param keys
     * @return
     */

    public Set<String> sdiff(String... keys) {
        try {
            if (ArrayUtils.isEmpty(keys)) {
                return Collections.emptySet();
            }

            Set<String> keySet = new HashSet<String>();
            for (String key : keys) {
                keySet.add(this.genKey(key));
            }

            return redisDataSource.sdiff(keySet.toArray(new String[0]));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-返回给定所有集合的交集
     *
     * @param keys
     * @return
     */

    public Set<String> sinter(String... keys) {
        try {
            if (ArrayUtils.isEmpty(keys)) {
                return Collections.emptySet();
            }

            Set<String> keySet = new HashSet<String>();
            for (String key : keys) {
                keySet.add(this.genKey(key));
            }
            return redisDataSource.sinter(keySet.toArray(new String[0]));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-返回给定所有集合的并集
     *
     * @param keys
     * @return
     */

    public Set<String> sunion(String... keys) {
        try {
            if (ArrayUtils.isEmpty(keys)) {
                return Collections.emptySet();
            }

            Set<String> keySet = new HashSet<String>();
            for (String key : keys) {
                keySet.add(this.genKey(key));
            }
            return redisDataSource.sunion(keySet.toArray(new String[0]));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-移除并返回key集合的的一个随机元素
     *
     * @param key
     * @return
     */

    public String spop(String key) {
        try {
            return redisDataSource.spop(this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-移除并返回key集合的的count个随机元素
     *
     * @param key
     * @param count
     * @return
     */

    public Set<String> spop(String key, long count) {
        try {
            return redisDataSource.spop(this.genKey(key), count);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-返回集合中一个随机元素
     *
     * @param key
     * @return
     */

    public String srandmember(String key) {
        try {
            return redisDataSource.srandmember(this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-返回集合中count个随机元素
     *
     * @param key
     * @param count
     * @return
     */

    public List<String> srandmember(String key, int count) {
        try {
            return redisDataSource.srandmember(this.genKey(key), count);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-移除key集合指定元素
     *
     * @param key
     * @param members
     * @return
     */

    public Long srem(String key, String... members) {
        try {
            return redisDataSource.srem(this.genKey(key), members);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }


    public Long zadd(String key, double score, String member) {
        try {
            return redisDataSource.zadd(this.genKey(key), score, member);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public <T> Long zadd(T t) throws MissAnnotationException {
        try {
            return redisDataSource.zadd(t);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public Long zcard(String key) {
        try {
            return redisDataSource.zcard(this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public List<String> zrange(String key, Long start, Long stop) {
        try {
            return redisDataSource.zrange(this.genKey(key), start, stop);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取有序集合所有的元素
     *
     * @param key
     * @return
     */
    public List<String> zrangeAll(String key) {
        try {
            return redisDataSource.zrangeAll(this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }


    public <T> List<T> zrangeAll4Obj(T t, String key) {
        try {
            return redisDataSource.zrangeAll4Obj(t, this.genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-单属性设置哈希key的值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */

    public Long hset(String key, String field, String value) {
        try {
            return redisDataSource.hset(genKey(key), field, value);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取哈希key的field的值
     *
     * @param key
     * @param field
     * @return
     */

    public String hget(String key, String field) {
        try {
            return redisDataSource.hget(genKey(key), field);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取key的所有哈希值
     *
     * @param key
     * @return
     */

    public Map<String, String> hgetAll(String key) {
        try {
            return redisDataSource.hgetAll(genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据key和对象初始值,获取对象转换
     *
     * @param t
     * @param key
     * @return
     */

    public <V> V hgetAll(V t, String key) {
        try {
            return redisDataSource.hgetAll(t, genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取哈希表key的所有字段
     *
     * @param key
     * @return
     */

    public Set<String> hkeys(String key) {
        try {
            return redisDataSource.hkeys(genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取哈希表key的所有值
     *
     * @param key
     * @return
     */

    public List<String> hvals(String key) {
        try {
            return redisDataSource.hvals(genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取哈希表中字段的数量
     *
     * @param key
     * @return
     */

    public Long hlen(String key) {
        try {
            return redisDataSource.hlen(genKey(key));
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取哈希表key给定字段的值
     *
     * @param key
     * @param fields
     * @return
     */

    public List<String> hmget(String key, String... fields) {
        try {
            return redisDataSource.hmget(genKey(key), fields);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取key,指定属性并返回对象
     *
     * @param t
     * @param key
     * @param fields
     * @return
     */

    public <V> V hmget(V t, String key, String... fields) {
        try {
            return redisDataSource.hmget(t, genKey(key), fields);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-多属性设置哈希的值 key为KeyMapPair-key hash为KeyMapPair-map
     *
     * @param keyMapPair
     * @return
     */

    public String hmset(KeyMapPair keyMapPair) {
        try {
            return redisDataSource.hmset(keyMapPair);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-设置hash对象值hash表中
     *
     * @param t
     * @return
     */

    public <V> String hmset(V t) {
        try {
            return redisDataSource.hmset(t);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public <T> String hmsetAndExpire(T t, int seconds) {
        try {
            return redisDataSource.hmsetAndExpire(t, seconds);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 哈希-多属性设置哈希key的值
     *
     * @param key
     * @param hash
     * @return
     */

    public String hmset(String key, Map<String, String> hash) {
        try {
            return redisDataSource.hmset(genKey(key), hash);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-只有在字段field不存在时,设置哈希表字段的值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */

    public Long hsetnx(String key, String field, String value) {
        try {
            return redisDataSource.hsetnx(genKey(key), field, value);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-为哈希表key指定的field字段加上增加值value
     *
     * @param key
     * @param field
     * @param value
     * @return
     */

    public Long hincrBy(String key, String field, long value) {
        try {
            return redisDataSource.hincrBy(genKey(key), field, value);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-查询哈希表key中,指定的field是否存在
     *
     * @param key
     * @param field
     * @return
     */

    public Boolean hexists(String key, String field) {
        try {
            return redisDataSource.hexists(genKey(key), field);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希表-删除一个或多个哈希表key的字段
     *
     * @param key
     * @param fields
     */

    public Long hdel(String key, String... fields) {
        try {
            return redisDataSource.hdel(genKey(key), fields);
        } catch (Exception e) {
            LoggerConfigs.REDIS_LOG.error(e.getMessage(), e);
        }
        return null;
    }
}
