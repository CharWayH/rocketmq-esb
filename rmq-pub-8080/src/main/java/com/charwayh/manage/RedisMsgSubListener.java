package com.charwayh.manage;

import com.charwayh.constant.Constant;
import com.charwayh.thread.SubscribeMsgThread;
import com.charwayh.util.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;

/**
 * Description: REDIS订阅实现方法
 * Author: SangYD
 * Date: 2019-05-31 11:20
 * Copyright (c) 2019, ewell.com
 * All Rights Reserved.
 */
public class RedisMsgSubListener extends JedisPubSub {

    private static Logger logger = LoggerFactory.getLogger(RedisMsgSubListener.class);

    // 取得订阅的消息后的处理
    @Override
    public void onMessage(String channel, String message) {
        if (StringUtils.isNotBlank(message)) {
            try {
                if (Constant.RedisPubSubKey.UPDATECACHE.equals(channel)) {
                    CacheManage cacheManage = SpringContextUtil.getBean(CacheManage.class);
                    Method method      = CacheManage.class.getMethod(message, null);
                    if (method != null) {
                        method.invoke(cacheManage);
                    } else {
                        cacheManage.cacheAllData();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                unsubscribe(channel);
                logger.error("将在10秒后重新开始对频道{}的监听", channel);
                try {
                    Thread.sleep(10000);
                    logger.error("重启对频道{}的监听", channel);
                    Executors.newCachedThreadPool().execute(new SubscribeMsgThread());
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }



    @Override
    public void unsubscribe(String... channels) {
        super.unsubscribe(channels);
        logger.error("redis发布订阅后，更新系统缓存时发生异常，停止对频道{}的监听", channels);
    }
}
