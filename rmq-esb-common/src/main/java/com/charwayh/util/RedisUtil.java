package com.charwayh.util;

//import com.ctrip.framework.apollo.util.ConfigUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.util.Pool;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * redis 工具类
 * hb
 */
public class RedisUtil {
    private static          Logger        logger      = LoggerFactory.getLogger(RedisUtil.class);
    private static          Pool<Jedis>   pool        = null;
    private static volatile RedisUtil     redisUtil   = null;
    private static          Boolean       useSentinel = false;
    private static          AtomicBoolean INIT_LOCK   = new AtomicBoolean();


    // pool 切换
    public static RedisUtil getRedisUtil() {
        if (!INIT_LOCK.get()) {
            synchronized (RedisUtil.class) {
                if (redisUtil == null) {
                    redisUtil = new RedisUtil();
                }
            }
        }
        return redisUtil;
    }


    private static final String IP       = "IP";
    private static final String PORT     = "PORT";
    private static final String PASSWORD = "PASSWORD";

    RedisUtil() {
        pool = init();
    }

    //初始化pool
    private static Pool<Jedis> init() {
        //适配,,判断是使用[哨兵+主从模式]还是[单节点模式],默认单节点模式
        try {
            String sentinel = null;
//            String sentinel = ConfigUtil.getApProperties("lood.redis.sentinel");
            if (StringUtils.isNotBlank(sentinel)) {
                useSentinel = Boolean.valueOf(sentinel);
            }
        } catch (Exception e) {
            //ignore
        }
        if (useSentinel) {
            //SentinelPool.init();
            return null;
        } else {
            return initPool();
        }
    }

    //初始化普通pool
    private static JedisPool initPool() {
//        String              redisAddress = ConfigUtil.getApProperties("lood.redis.address");
        String              redisAddress = "192.168.1.184:6379";
        Map<String, Object> configMap    = parseRedisConfig(redisAddress);
        String              ip           = (String) configMap.get(IP);
        int                 port         = (Integer) configMap.get(PORT);
        String              password     = (String) configMap.get(PASSWORD);

        JedisPoolConfig config = new JedisPoolConfig();
        // 控制一个pool可分配多少个jedis实例，通过getResource()来获取；
        // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
//        config.setMaxTotal(Integer.parseInt(ConfigUtil.getApProperties("lood.redis.maxTotal", "100")));
        // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(20);
        // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(1000 * 100);
        config.setTestOnBorrow(true);
        if (StringUtils.isEmpty(password)) {
            return new JedisPool(config, ip, port, 10000);
        } else {
            return new JedisPool(config, ip, port, 10000, password);
        }
    }

    //初始化redis哨兵pool
//    private static JedisSentinelPool initSentinelPool() {
//        String redisAddress = ConfigUtil.getApProperties("lood.redis.sentinel.address");
//        String masterName   = ConfigUtil.getApProperties("lood.redis.sentinel.master");
//        String password     = ConfigUtil.getApProperties("lood.redis.sentinel.auth.password");
//        if (StringUtils.isEmpty(redisAddress) || StringUtils.isEmpty(masterName)) {
//            throw new NullPointerException("redis哨兵配置信息为空,redisAddress:" + redisAddress + "   masterName: " + masterName + " ");
//        }
//
//        Set<String> sentinels = new HashSet<String>();
//        String[]    addresses = redisAddress.split(",");
//        try {
//            for (String address : addresses) {
//                String[] ipAndPort = address.split(":");
//                sentinels.add(new HostAndPort(ipAndPort[0], Integer.valueOf(ipAndPort[1])).toString());
//            }
//        } catch (Exception e) {
//            throw new NullPointerException("redis哨兵路径格式不合法,预期格式: ip1:port1,ip2:port2,ip3:port3 ,实际格式:" + redisAddress);
//        }
//        if (StringUtils.isNotEmpty(password)) {
//            return new JedisSentinelPool(masterName, sentinels, password);
//        } else {
//            return new JedisSentinelPool(masterName, sentinels);
//        }
//    }


    private static Map<String, Object> parseRedisConfig(String redisAddress) {
        Map<String, Object> map      = new HashMap<String, Object>();
        String              password = null;
        String              ip       = null;
        int                 port     = 6379;

        try {
            int atPosition       = redisAddress.indexOf("@");
            int separatorPositon = redisAddress.indexOf(":");
            if (atPosition == -1) {
                ip = redisAddress.substring(0, separatorPositon);
                port = Integer.parseInt(redisAddress.substring(separatorPositon + 1));
            } else {
                password = redisAddress.substring(0, atPosition);
                ip = redisAddress.substring(atPosition + 1, separatorPositon);
                port = Integer.parseInt(redisAddress.substring(separatorPositon + 1));
            }
        } catch (Exception e) {
            logger.error("redis地址配置有误:{}", e);
            logger.error("redis地址配置正确格式:{}->如 {}", "lood.redis.address=password@ip:port", "111111@127.0.0.1:6379");
        }

        logger.debug("ip->{},port->{},passwrod->{}", ip, port, password);
        map.put(IP, ip);
        map.put(PORT, port);
        map.put(PASSWORD, password);
        return map;
    }

    //选择pool
    private static Pool<Jedis> getMasterResource() {
        if (useSentinel) {
//            return SentinelPool.getMasterJedisPool();
            return null;
        } else {
            return pool;
        }
    }

    private static Pool<Jedis> getSlaveResource() {
        if (useSentinel) {
//            return SentinelPool.getSlaveJedisPool();
            return null;
        } else {
            return pool;
        }
    }

    public Long incr(String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        value       = null;
        try {
            jedis = currentPool.getResource();
            value = jedis.incr(key);
        } catch (Exception e) {
            logger.error("Method[incr]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return value;
    }

    public Long incrBy(String key, long increment) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        value       = null;
        try {
            jedis = currentPool.getResource();
            value = jedis.incrBy(key, increment);
        } catch (Exception e) {
            logger.error("Method[incr]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return value;
    }

    public List<String> blpop(int timeout, String key) {
        Pool<Jedis>  currentPool = getMasterResource();
        Jedis        jedis       = null;
        List<String> value       = null;
        try {
            jedis = currentPool.getResource();
            value = jedis.blpop(timeout, key);
        } catch (Exception e) {
            logger.error("Method[incr]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return value;
    }


    /**
     * <p>通过key获取储存在redis中的value</p>
     * <p>并释放连接</p>
     *
     * @param key
     * @return 成功返回value 失败返回null
     */
    public String get(String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        String      value       = null;
        try {
            jedis = currentPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            logger.error("Method[get]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return value;
    }

    /**
     * <p>通过key获取储存在redis中的value</p>
     * <p>并释放连接</p>
     *
     * @param key
     * @return 成功返回value 失败返回null
     */
    public byte[] get(byte[] key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        byte[]      value       = null;
        try {
            jedis = currentPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            logger.error("Method[get]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return value;
    }

    /**
     * @Author SangYD
     * @Description 查询redis中某个key的剩余存活时间
     * @Date 13:21 2018/12/5
     * @Param [key]
     * @Return java.lang.Long
     **/
    public Long ttl(String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            if (StringUtils.isEmpty(key)) {
                return -1L;
            }
            jedis = currentPool.getResource();
            return jedis.ttl(key);
        } catch (Exception e) {
            logger.error("Method[ttl]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return -1L;
    }

    public Long expire(String key, int seconds) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            if (StringUtils.isEmpty(key)) {
                return -1L;
            }
            jedis = currentPool.getResource();
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            logger.error("Method[ttl]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return -1L;
    }

    /**
     * 根据key拿到整个key的所有值
     *
     * @param key
     */
    public List<String> lpopAll(String key) {
        return lpop(key, -1);
    }

    /**
     * <p>通过key从list的头部删除一个value,并返回该value</p>
     *
     * @param key
     * @return
     */
    public String lpop(String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        String      res         = null;
        try {
            if (StringUtils.isEmpty(key)) {
                return null;
            }
            jedis = currentPool.getResource();
            res = jedis.lpop(key);
        } catch (Exception e) {
            logger.error("Method[lpop]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key从list的头部删除一个value,并返回该value</p>
     *
     * @param key
     * @return
     */
    public byte[] lpop(byte[] key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        byte[]      res         = null;
        try {
            if (key == null || key.length == 0) {
                return null;
            }
            jedis = currentPool.getResource();
            res = jedis.lpop(key);
        } catch (Exception e) {
            logger.error("Method[lpop]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key从list的头部删除len个value,并返回该value</p>
     *
     * @param key
     * @param len
     * @return
     */
    public List<String> lpop(String key, int len) {
        Pool<Jedis> currentPool = getMasterResource();
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        Jedis        jedis = null;
        List<String> list  = new ArrayList<>();
        try {
            jedis = currentPool.getResource();
            Transaction ts = jedis.multi();
            if (len == -1) {
                ts.lrange(key, 0, -1);
//                long length = jedis.llen(key);
//                int counter = 0;
//                int left = 1000;
//                while(counter < length){
//                    ts.ltrim(key, left, length);
//                    counter += left;
//                }
//                ts.ltrim(key, len, -1);
                ts.del(key);
            } else {
                ts.lrange(key, 0, len - 1);
                ts.ltrim(key, len, -1);
            }
            list = (ArrayList<String>) ts.exec().get(0);
        } catch (Exception e) {
            logger.error("Method[lpop]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return list;
    }


    /**
     * 数据放入redis中
     *
     * @param key
     * @param val
     * @return
     */
    public long rpush(String key, List<String> val) {
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(val)) {
            return 0;
        }
        if (val.size() < 1) {
            return 0;
        }
        return rpush(key, CommonUtils.listToArray(val));
    }

    /**
     * 数据放入redis中 并带入过期时间
     *
     * @param key
     * @param val
     * @return
     */
    public long rpush(String key,int seconds, List<String> val) {
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(val)) {
            return 0;
        }
        if (val.size() < 1) {
            return 0;
        }
        return rpush(key,seconds, CommonUtils.listToArray(val));
    }

    /**
     * <p>通过key向list尾部添加字符串</p>
     *
     * @param key
     * @param strs 可以使一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public Long rpush(String key, String... strs) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        res         = null;
        try {
            if (strs == null || StringUtils.isEmpty(key)) {
                return 0L;
            }
            if (strs.length < 1) {
                return 0L;
            }
            jedis = currentPool.getResource();
            res = jedis.rpush(key, strs);
        } catch (Exception e) {
            logger.error("Method[rpush]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }
    /**
     * <p>通过key向list尾部添加字符串</p>
     *
     * @param key
     * @param strs 可以使一个string 也可以是string数组
     * seconds 过期时间，单位是秒
     * @return 返回list的value个数
     */
    public Long rpush(String key,int seconds, String... strs) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        res         = null;
        try {
            if (strs == null || StringUtils.isEmpty(key)) {
                return 0L;
            }
            if (strs.length < 1) {
                return 0L;
            }
            jedis = currentPool.getResource();
            res = jedis.rpush(key, strs);
            //设置key 过期时间
            jedis.expire(key, seconds);
        } catch (Exception e) {
            logger.error("Method[rpush]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }
    /**
     * <p>通过多个key，多个list,向key对应list尾部添加字符串</p>
     *
     * @param keyValue map的key对应redis中的key，values 对应需要添加的值
     * @return 返回list的value个数
     * seconds 过期时间，单位是秒
     */
    public long rpush(Map<String, List<String>> keyValue, int seconds) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        long        res         = 0;
        try {
            if (MapUtils.isEmpty(keyValue)) {
                return 0L;
            }
            if (keyValue.size() < 1) {
                return 0L;
            }
            jedis = currentPool.getResource();
            for (String key : keyValue.keySet()) {
                res += jedis.rpush(key, CommonUtils.listToArray(keyValue.get(key)));
                //设置key 过期时间
                jedis.expire(key, seconds);
            }
        } catch (Exception e) {
            logger.error("Method[rpush]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过多个key，多个list,向key对应list尾部添加字符串</p>
     *
     * @param keyValue map的key对应redis中的key，values 对应需要添加的值
     * @return 返回list的value个数
     */
    public Long rpush(Map<String, List<String>> keyValue) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        long        res         = 0;
        try {
            if (MapUtils.isEmpty(keyValue)) {
                return 0L;
            }
            if (keyValue.size() < 1) {
                return 0L;
            }

            jedis = currentPool.getResource();
            for (String key : keyValue.keySet()) {
                res += jedis.rpush(key, CommonUtils.listToArray(keyValue.get(key)));
            }
        } catch (Exception e) {
            logger.error("Method[rpush]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>向redis存入key和value,并释放连接资源</p>
     * <p>如果key已经存在 则覆盖</p>
     *
     * @param key
     * @param value
     * @return 成功 返回OK 失败返回 0
     */
    public String set(String key, String value) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
                return null;
            }
            jedis = currentPool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {
            logger.error("Method[set]错误信息：{}", e.getMessage(), e);
            return "0";
        } finally {
            returnResource(currentPool, jedis);
        }
    }


    /**
     * <p>向redis存入key和value,并释放连接资源</p>
     * <p>如果key已经存在 则覆盖</p>
     *
     * @param key
     * @param value
     * @param seconds 过期时间，单位是秒
     * @return 成功 返回OK 失败返回 0
     */
    public String set(byte[] key, byte[] value, int seconds) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            if (key == null || value == null || key.length == 0 || value.length == 0) {
                return null;
            }
            jedis = currentPool.getResource();
            String result = jedis.set(key, value);
            jedis.expire(key, seconds);
            return result;
        } catch (Exception e) {
            logger.error("Method[set]错误信息：{}", e.getMessage(), e);
            return "0";
        } finally {
            returnResource(currentPool, jedis);
        }
    }


    /**
     * <p>向redis存入key和value,并释放连接资源</p>
     * <p>如果key已经存在 则覆盖</p>
     *
     * @param key
     * @param value
     * @return 成功 返回OK 失败返回 0
     */
    public String set(byte[] key, byte[] value) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            if (key == null || value == null || key.length == 0 || value.length == 0) {
                return null;
            }
            jedis = currentPool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {
            logger.error("Method[set]错误信息：{}", e.getMessage(), e);
            return "0";
        } finally {
            returnResource(currentPool, jedis);
        }
    }

    /**
     * <p>删除指定的key,也可以传入一个包含key的数组</p>
     *
     * @param keys 一个key  也可以使 string 数组
     * @return 返回删除成功的个数
     */
    public Long del(String... keys) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.del(keys);
        } catch (Exception e) {
            logger.error("Method[del]错误信息：{}", e.getMessage(), e);
            return 0L;
        } finally {
            returnResource(currentPool, jedis);
        }
    }

    /**
     * <p>删除指定的key,也可以传入一个包含key的数组</p>
     *
     * @param key 一个key  也可以使 byte 数组
     * @return 返回删除成功的个数
     */
    public Long del(byte[] key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.del(key);
        } catch (Exception e) {
            logger.error("Method[del]错误信息：{}", e.getMessage(), e);
            return 0L;
        } finally {
            returnResource(currentPool, jedis);
        }
    }

    /**
     * <p>通过key向指定的value值追加值</p>
     *
     * @param key
     * @param str
     * @return 成功返回 添加后value的长度 失败 返回 添加的 value 的长度  异常返回0L
     */
    public Long append(String key, String str) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.append(key, str);
        } catch (Exception e) {
            logger.error("Method[append]错误信息：{}", e.getMessage(), e);
            return 0L;
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>判断key是否存在</p>
     *
     * @param key
     * @return true OR false
     */
    public Boolean exists(String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error("Method[exists]错误信息：{}", e.getMessage(), e);
            return false;
        } finally {
            returnResource(currentPool, jedis);
        }
    }

    /**
     * <p>判断key是否存在</p>
     *
     * @param key
     * @return true OR false
     */
    public Boolean exists(byte[] key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error("Method[exists]错误信息：{}", e.getMessage(), e);
            return false;
        } finally {
            returnResource(currentPool, jedis);
        }
    }

    /**
     * <p>设置key value,如果key已经存在则返回0,nx==> not exist</p>
     *
     * @param key
     * @param value
     * @return 成功返回1 如果存在 和 发生异常 返回 0
     */
    public Long setnx(String key, String value) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.setnx(key, value);
        } catch (Exception e) {
            logger.error("Method[setnx]错误信息：{}", e.getMessage(), e);
            return 0L;
        } finally {
            returnResource(currentPool, jedis);
        }
    }

    /**
     * 获取锁
     *
     * @param key
     * @param requestId       加锁者
     * @param secondsToExpire 过期时间
     * @return boolean
     * @author YuanZX
     * @date 2020/3/12
     */
    public boolean lock(String key, String requestId, int secondsToExpire) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            SetParams params = new SetParams();
            params.nx();
            params.ex(secondsToExpire);
            String statusCode = jedis.set(key, requestId, params);
            return "OK".equals(statusCode);
        } catch (Exception e) {
            logger.error("Method[lock]错误信息：{}", e.getMessage(), e);
            return false;
        } finally {
            returnResource(currentPool, jedis);
        }
    }

    public boolean tryLock(String key, String requestId, int secondsToExpire) {
        long end = System.currentTimeMillis() + secondsToExpire * 1000;
        while (System.currentTimeMillis() < end) {
            if (lock(key, requestId, secondsToExpire)) {
                return true;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

    public String tryLock(String key, int secondsToExpire) {
        String requestId = UUID.randomUUID().toString();
        if (tryLock(key, requestId, secondsToExpire)) {
            return requestId;
        }
        return null;
    }

    /**
     * 释放锁
     *
     * @param key
     * @param requestId 释放者
     * @return boolean
     * @author YuanZX
     * @date 2020/3/12
     */
    public boolean unlock(String key, String requestId) {
        final Long  RELEASE_SUCCESS = 1L;
        Pool<Jedis> currentPool     = getMasterResource();
        Jedis       jedis           = null;
        try {
            jedis = currentPool.getResource();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(requestId));
            return RELEASE_SUCCESS.equals(result);
        } catch (Exception e) {
            logger.error("Method[unlock]错误信息：{}", e.getMessage(), e);
            return false;
        } finally {
            returnResource(currentPool, jedis);
        }
    }

    /**
     * <p>设置key value并制定这个键值的有效期</p>
     *
     * @param key
     * @param value
     * @param seconds 单位:秒
     * @return 成功返回OK 失败和异常返回null
     */
    public String setex(String key, String value, int seconds) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        String      res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.setex(key, seconds, value);
        } catch (Exception e) {

            logger.error("Method[setex]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过批量的key获取批量的value</p>
     *
     * @param keys string数组 也可以是一个key
     * @return 成功返回value的集合, 失败返回null的集合 ,异常返回空
     */
    public List<String> mget(String... keys) {
        Pool<Jedis>  currentPool = getMasterResource();
        Jedis        jedis       = null;
        List<String> values      = null;
        try {
            jedis = currentPool.getResource();
            values = jedis.mget(keys);
        } catch (Exception e) {

            logger.error("Method[mget]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return values;
    }

    /**
     * <p>批量的设置key:value,可以一个</p>
     * <p>example:</p>
     * <p>  obj.mset(new String[]{"key2","value1","key2","value2"})</p>
     *
     * @param keysvalues
     * @return 成功返回OK 失败 异常 返回 null
     */
    public String mset(String... keysvalues) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        String      res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.mset(keysvalues);
        } catch (Exception e) {

            logger.error("Method[mset]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>设置key的值,并返回一个旧值</p>
     *
     * @param key
     * @param value
     * @return 旧值 如果key不存在 则返回null
     */
    public String getset(String key, String value) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        String      res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.getSet(key, value);
        } catch (Exception e) {

            logger.error("Method[getset]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过下标 和key 获取指定下标位置的 value</p>
     *
     * @param key
     * @param startOffset 开始位置 从0 开始 负数表示从右边开始截取
     * @param endOffset
     * @return 如果没有返回null
     */
    public String getrange(String key, int startOffset, int endOffset) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        String      res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.getrange(key, startOffset, endOffset);
        } catch (Exception e) {

            logger.error("Method[getrange]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key获取value值的长度</p>
     *
     * @param key
     * @return 失败返回null
     */
    public Long serlen(String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.strlen(key);
        } catch (Exception e) {

            logger.error("Method[serlen]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key返回所有和key有关的value</p>
     *
     * @param key
     * @return
     */
    public List<String> hvals(String key) {
        Pool<Jedis>  currentPool = getMasterResource();
        Jedis        jedis       = null;
        List<String> res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.hvals(key);
        } catch (Exception e) {

            logger.error("Method[hvals]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key向list头部添加字符串</p>
     *
     * @param key
     * @param strs 可以使一个string 也可以使string数组
     * @return 返回list的value个数
     */
    public Long lpush(String key, String... strs) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.lpush(key, strs);
        } catch (Exception e) {

            logger.error("Method[lpush]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key设置list指定下标位置的value</p>
     * <p>如果下标超过list里面value的个数则报错</p>
     *
     * @param key
     * @param index 从0开始
     * @param value
     * @return 成功返回OK
     */
    public String lset(String key, Long index, String value) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        String      res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.lset(key, index, value);
        } catch (Exception e) {

            logger.error("Method[lset]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key保留list中从strat下标开始到end下标结束的value值</p>
     *
     * @param key
     * @param start
     * @param end
     * @return 成功返回OK
     */
    public String ltrim(String key, long start, long end) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        String      res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.ltrim(key, start, end);
        } catch (Exception e) {

            logger.error("Method[ltrim]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key从list尾部删除一个value,并返回该元素</p>
     *
     * @param key
     * @return
     */
    public String rpop(String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        String      res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.rpop(key);
        } catch (Exception e) {

            logger.error("Method[rpop]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key获取list中指定下标位置的value</p>
     *
     * @param key
     * @param index
     * @return 如果没有返回null
     */
    public String lindex(String key, long index) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        String      res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.lindex(key, index);
        } catch (Exception e) {

            logger.error("Method[lindex]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key返回list的长度</p>
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.llen(key);
        } catch (Exception e) {

            logger.error("Method[llen]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key返回hashset方式的value的长度</p>
     *
     * @param key
     * @return
     */
    public Long hlen(String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.hlen(key);
        } catch (Exception e) {

            logger.error("Method[llen]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key获取list指定下标位置的value</p>
     * <p>如果start 为 0 end 为 -1 则返回全部的list中的value</p>
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        Pool<Jedis>  currentPool = getMasterResource();
        Jedis        jedis       = null;
        List<String> res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.lrange(key, start, end);
        } catch (Exception e) {

            logger.error("Method[lrange]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * <p>通过key删除给定区间内的元素</p>
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByRank(String key, long start, long end) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.zremrangeByRank(key, start, end);
        } catch (Exception e) {

            logger.error("Method[zremrangeByRank]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * 拿到redis中所有有的key
     *
     * @return
     */
    public Set<String> getKeys() {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.keys("*");
        } catch (Exception e) {
            logger.error("Method[String]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return null;
    }

    /**
     * 拿到redis中所有有的key
     *
     * @return
     */
    public Set<String> getKeys(String keyPattern) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.keys(keyPattern);
        } catch (Exception e) {
            logger.error("Method[String]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return null;
    }

    /**
     * 往一个set中插入数据
     *
     * @return
     */
    public long sAdd(String key, String ...members) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.sadd(key, members);
        } catch (Exception e) {
            logger.error("Method[String]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return 0L;
    }

    /**
     * 移除一个set中对的数据
     * @param key
     * @param members
     */
    public void delCacheSetMem(String key, String ...members) {
        if (key == null) {
            return;
        }
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            jedis.srem(key, members);
        } catch (Exception e) {
            logger.error("Method[String]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
    }

    /**
     * 取出一个set中的所有值
     *
     * @return
     */
    public Set<String> getSMembers(String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.smembers(key);
        } catch (Exception e) {
            logger.error("Method[String]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return null;
    }

    public Long hset(String key, String field, String value) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error("Method[hset]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return null;
    }

    public Long hset(String key, String field, String value, int seconds) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            Long result = jedis.hset(key, field, value);
            jedis.expire(key, seconds);
            return result;
        } catch (Exception e) {
            logger.error("Method[hset]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return null;
    }


    public String hget(final String key, final String field) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.hget(key, field);
        } catch (Exception e) {
            logger.error("Method[hset]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return null;
    }


    public Map<String, String> hgetAll(final String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.hgetAll(key);
        } catch (Exception e) {
            logger.error("Method[hset]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return null;
    }

    public Long hdel(final String key, final String field) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            return jedis.hdel(key, field);
        } catch (Exception e) {
            logger.error("Method[hset]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return null;
    }


    /**
     * 发布一个消息
     *
     * @param channel
     * @param message
     */
    public void publishMsg(String channel, String message) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            jedis.publish(channel, message);
        } catch (Exception e) {
            logger.error("Method[publishMsg]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
    }

    /**
     * 接收消息。在main方法调用后，会一直执行下去。当有发布对应消息时，就会在jedisPubSub中接收到！
     *
     * @param jedisPubSub
     * @param channels
     */
    public void subscribeMsg(JedisPubSub jedisPubSub, String... channels) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            jedis.subscribe(jedisPubSub, channels);
        } catch (Exception e) {
            logger.error("Method[subscribeMsg]错误信息：{}", e.getMessage(), e);
            try {
                logger.error("Method[subscribeMsg]10秒钟后将再次尝试重新订阅：{}", channels);
                Thread.sleep(10000);
                subscribeMsg(jedisPubSub, channels);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } finally {
            returnResource(currentPool, jedis);
        }
    }

    /**
     * 订阅redis中的已有事件
     *
     * @param jedisPubSub
     * @param channels
     */
    public void psubscribe(JedisPubSub jedisPubSub, String channels) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        try {
            jedis = currentPool.getResource();
            jedis.psubscribe(jedisPubSub, channels);
        } catch (Exception e) {
            logger.error("Method[psubscribe]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
    }


    public String info() {
        Jedis  jedis = null;
        String info  = null;
        try {
            jedis = pool.getResource();
            info = jedis.info();
        } catch (Exception e) {
            logger.error("Method[info]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(pool, jedis);
        }
        return info;
    }

    /**
     * 添加成员到zset
     *
     * @param key
     * @param scoreMembers map
     * @return java.lang.Object
     * @author YuanZX
     * @date 14:05 2018/10/10
     */
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            logger.error("Method[String]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * 删除zset中指定成员
     *
     * @param key
     * @param members
     * @return java.lang.Long
     * @author YuanZX
     * @date 15:03 2018/10/10
     */
    public Long zrem(String key, String[] members) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        res         = null;
        try {
            jedis = currentPool.getResource();
            res = jedis.zrem(key, members);
        } catch (Exception e) {
            logger.error("Method[String]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return res;
    }

    /**
     * 获取zset中指定范围（排名）的成员
     * 正序
     *
     * @param key
     * @param start 0代表第一个
     * @param end   -1代表最后一个
     * @return LinkedHashSet java.cc.ewell.sdk.util.Set<java.lang.String>
     * @author YuanZX
     * @date 16:16 2018/10/10
     */
    public Set<String> zrange(String key, long start, long end) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Set<String> membersSet  = new LinkedHashSet<>();
        try {
            jedis = currentPool.getResource();
            membersSet = jedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error("Method[String]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return membersSet;
    }

    /**
     * 获取zset中指定范围（排名）的成员
     * 倒序
     *
     * @param key
     * @param start 0代表第一个
     * @param end   -1代表最后一个
     * @return LinkedHashSet java.cc.ewell.sdk.util.Set<java.lang.String>
     * @author YuanZX
     * @date 16:16 2018/10/10
     */
    public Set<String> zrevrange(String key, long start, long end) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Set<String> membersSet  = new LinkedHashSet<String>();
        try {
            jedis = currentPool.getResource();
            membersSet = jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("Method[String]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return membersSet;
    }

    /**
     * 获取zset成员个数
     *
     * @param key
     * @return java.lang.Long
     * @author YuanZX
     * @date 16:27 2018/10/10
     */
    public Long zcard(String key) {
        Pool<Jedis> currentPool = getMasterResource();
        Jedis       jedis       = null;
        Long        size        = null;
        try {
            jedis = currentPool.getResource();
            size = jedis.zcard(key);
        } catch (Exception e) {
            logger.error("Method[String]错误信息：{}", e.getMessage(), e);
        } finally {
            returnResource(currentPool, jedis);
        }
        return size;
    }

    /**
     * 返还到连接池
     *
     * @param pool
     * @param jedis
     */
    public static void returnResource(Pool<Jedis> pool, Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
