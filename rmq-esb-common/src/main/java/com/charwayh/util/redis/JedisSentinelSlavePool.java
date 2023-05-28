package com.charwayh.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.util.redis
 * @date:2023/5/28
 */
@Slf4j
public class JedisSentinelSlavePool {
}
