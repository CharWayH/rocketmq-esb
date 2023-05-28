package com.charwayh.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.util.*;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.util.redis
 * @date:2023/5/28
 */



@Slf4j
public class JedisSlaveFactory implements PooledObjectFactory<Jedis> {
    private final Set<String> sentinels;
    private final String masterName;
    private final int connectionTimeout;
    private final int soTimeout;
    private final String password;
    private final int database;
    private final String clientName;
    private final boolean ssl;
    private final SSLSocketFactory sslSocketFactory;
    private SSLParameters sslParameters;
    private HostnameVerifier hostnameVerifier;
    private Random random;

    public JedisSlaveFactory(final Set<String> sentinels, final String masterName, final int connectionTimeout,
                             final int soTimeout, final String password, final int database, final String clientName,
                             final boolean ssl, final SSLSocketFactory sslSocketFactory, final SSLParameters sslParameters,
                             final HostnameVerifier hostnameVerifier) {
        this.sentinels = sentinels;
        this.masterName = masterName;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.password = password;
        this.database = database;
        this.clientName = clientName;
        this.ssl = ssl;
        this.sslSocketFactory = sslSocketFactory;
        this.sslParameters = sslParameters;
        this.hostnameVerifier = hostnameVerifier;
        this.random = new Random();
    }

    @Override
    public void activateObject(PooledObject<Jedis> pooledJedis) throws Exception {
        final BinaryJedis jedis = pooledJedis.getObject();
        if (jedis.getDB() != database) {
            jedis.select(database);
        }
    }

    /**
     * 销毁redis底层连接
     */
    @Override
    public void destroyObject(PooledObject<Jedis> pooledJedis){
        log.debug("destroyObject =" + pooledJedis.getObject());
        final BinaryJedis jedis = pooledJedis.getObject();
        if (jedis.isConnected()) {
            try {
                jedis.quit();
                jedis.disconnect();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 创建Redis底层连接对象，返回池化对象.
     */
    @Override
    public PooledObject<Jedis> makeObject() {
        List<HostAndPort> slaves = this.getAlivedSlaves();

        //在slave节点中随机选取一个节点进行连接
        int index = slaves.size() == 1 ? 0 : random.nextInt(slaves.size());
        final HostAndPort hostAndPort = slaves.get(index);

        log.debug("Create jedis instance from slaves=[" + slaves + "] , choose=[" + hostAndPort + "]");

        //创建redis客户端
        final Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort(), connectionTimeout,
                soTimeout, ssl, sslSocketFactory, sslParameters, hostnameVerifier);

        //测试连接,设置密码,数据库.
        try {
            jedis.connect();
            if (null != this.password) {
                jedis.auth(this.password);
            }
            if (database != 0) {
                jedis.select(database);
            }
            if (clientName != null) {
                jedis.clientSetname(clientName);
            }
        } catch (JedisException je) {
            jedis.close();
            throw je;
        }

        return new DefaultPooledObject<Jedis>(jedis);
    }


    /**
     * 获取可用的RedisSlave节点信息
     */
    private List<HostAndPort> getAlivedSlaves() {
        log.debug("Get alived salves start...");

        List<HostAndPort> alivedSalaves = new ArrayList<>();
        boolean sentinelAvailable = false;

        //循环哨兵,建立连接获取slave节点信息
        //当某个哨兵连接失败，会忽略异常连接下一个哨兵
        for (String sentinel : sentinels) {
            final HostAndPort hap = HostAndPort.parseString(sentinel);

            log.debug("Connecting to Sentinel " + hap);

            Jedis jedis = null;
            try {
                jedis = new Jedis(hap.getHost(), hap.getPort());

                List<Map<String, String>> slavesInfo = jedis.sentinelSlaves(masterName);

                //可以连接到哨兵
                sentinelAvailable = true;

                //没有查询到slave信息,循环下一个哨兵
                if (slavesInfo == null || slavesInfo.size() == 0) {
                    log.warn("Cannot get slavesInfo, master name: " + masterName + ". Sentinel: " + hap
                            + ". Trying next one.");
                    continue;
                }

                //获取可用的Slave信息
                for (Map<String, String> slave : slavesInfo) {
                    if(slave.get("flags").equals("slave")) {
                        String host = slave.get("ip");
                        int port = Integer.valueOf(slave.get("port"));
                        HostAndPort hostAndPort = new HostAndPort(host, port);

                        log.info("Found alived redis slave:[" + hostAndPort + "]");

                        alivedSalaves.add(hostAndPort);
                    }
                }

                log.debug("Get alived salves end...");
                break;
            } catch (JedisException e) {
                //当前哨兵连接失败,忽略错误连接下一个哨兵
                log.warn("Cannot get slavesInfo from sentinel running @ " + hap + ". Reason: " + e
                        + ". Trying next one.");
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }

        //没有可用的slave节点信息
        if (alivedSalaves.isEmpty()) {
            if (sentinelAvailable) {
                throw new JedisException("Can connect to sentinel, but " + masterName
                        + " cannot find any redis slave");
            } else {
                throw new JedisConnectionException("All sentinels down");
            }
        }

        return alivedSalaves;
    }

    @Override
    public void passivateObject(PooledObject<Jedis> pooledJedis) {
    }

    /**
     * 检查jedis客户端是否有效
     * @param pooledJedis 池中对象
     * @return true有效  false无效
     */
    @Override
    public boolean validateObject(PooledObject<Jedis> pooledJedis) {
        final BinaryJedis jedis = pooledJedis.getObject();
        try {
            //是否TCP连接 && 是否ping通  && 是否slave角色
            boolean result = jedis.isConnected()
                    && jedis.ping().equals("PONG")
                    && jedis.info("Replication").contains("role:slave");

            log.debug("ValidateObject Jedis=["+jedis+"] host=[ " + jedis.getClient().getHost() +
                    "] port=[" + jedis.getClient().getPort() +"] return=[" + result + "]");
            return result;
        } catch (final Exception e) {
            log.warn("ValidateObject error jedis client cannot use", e);
            return false;
        }
    }

}
