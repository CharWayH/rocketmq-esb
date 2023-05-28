package com.charwayh.util.redis;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.util.redis
 * @date:2023/5/28
 */
public class SentinelPool {

    private static JedisSentinelPool jedisSentinelPool;

    private static JedisSentinelSlavePool jedisSentinelSlavePool;

    private static Set<String> sentinels = new HashSet<>();
    /**
     * 哨兵集群的名称
     */
    private static String      masterName;

    private static String password;

    @Value("${spring.redis.host}")
    private static String redisAddress;

    private static String sentinelMasterName;

    private static String pwd;

    public static void init() {
        /**初始化 reids的各个参数**/

        //哨兵的地址
        initParams();

        initPool();

    }

    //初始化redis哨兵pool
    private static void initParams() {
//        String redisAddress       = ConfigUtil.getApProperties("lood.redis.sentinel.address");
//        String sentinelMasterName = ConfigUtil.getApProperties("lood.redis.sentinel.master");
//        String pwd                = ConfigUtil.getApProperties("lood.redis.sentinel.auth.password");
        if (StringUtils.isEmpty(redisAddress) || StringUtils.isEmpty(sentinelMasterName)) {
            throw new NullPointerException("redis哨兵配置信息为空,redisAddress:" + redisAddress + "   masterName: " + masterName + " ");
        }
        //集群的名称
        masterName = sentinelMasterName;
        password = pwd;
        String[] addresses = redisAddress.split(",");
        try {
            for (String address : addresses) {
                String[] ipAndPort = address.split(":");
                sentinels.add(new HostAndPort(ipAndPort[0], Integer.valueOf(ipAndPort[1])).toString());
            }
        } catch (Exception e) {
            throw new NullPointerException("redis哨兵路径格式不合法,预期格式: ip1:port1,ip2:port2,ip3:port3 ,实际格式:" + redisAddress);
        }

    }

    private static void initPool() {

        GenericObjectPoolConfig jedisPoolConfig = new GenericObjectPoolConfig();
        //连接池中最大对象数量
        jedisPoolConfig.setMaxTotal(100);
        //最大能够保持idel状态的对象数
        jedisPoolConfig.setMaxIdle(1);
        //最小能够保持idel状态的对象数
        jedisPoolConfig.setMinIdle(1);
        //当池内没有可用资源,最大等待时长
        jedisPoolConfig.setMaxWaitMillis(3000);
        //表示有一个idle object evitor线程对object进行扫描,调用validateObject方法.
        jedisPoolConfig.setTestWhileIdle(true);
        //evitor线程对object进行扫描的时间间隔
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
        //表示对象的空闲时间，如果超过这个时间对象没有被使用则变为idel状态
        //然后才能被idle object evitor扫描并驱逐；
        //这一项只有在timeBetweenEvictionRunsMillis大于0时和setTestWhileIdle=true时才有意义
        //-1 表示对象不会变成idel状态
        jedisPoolConfig.setMinEvictableIdleTimeMillis(60000);
        //表示idle object evitor每次扫描的最多的对象数；
        jedisPoolConfig.setNumTestsPerEvictionRun(10);

        //在从池中获取对象时调用validateObject方法检查
        jedisPoolConfig.setTestOnBorrow(false);
        //在把对象放回池中时调用validateObject方法检查
        jedisPoolConfig.setTestOnReturn(false);

        //连接超时
        int timeout = 3000;


        //主节点写

        jedisSentinelPool = new JedisSentinelPool(masterName,//主节点名
                sentinels,//Sentinel节点集合
                jedisPoolConfig,//连接池配置
                timeout, password, 0);//连接超时设置
        //从节点读


//        jedisSentinelSlavePool = new JedisSentinelSlavePool(masterName, sentinels, jedisPoolConfig, timeout, password, 0);

    }


    public static Jedis getJedisMaster() {
        return jedisSentinelPool.getResource();
    }

//    public static Jedis getJedisSlave() {
//        return jedisSentinelSlavePool.getResource();
//    }

    public static Pool<Jedis> getMasterJedisPool() {
        return jedisSentinelPool;
    }

    public static Pool<Jedis> getSlaveJedisPool() {
        return null;
//        return jedisSentinelSlavePool;
    }


}

