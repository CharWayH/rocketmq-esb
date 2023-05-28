package com.charwayh.thread;

import com.charwayh.constant.Constant;
import com.charwayh.manage.CacheManage;
import com.charwayh.util.RedisUtil;

/**
 * Description:  redis的发布订阅线程
 * Author: SangYD
 * Date: 2019-12-02 11:01
 * Copyright (c) 2019, ewell.com
 * All Rights Reserved.
 */
public class SubscribeMsgThread extends Thread {

    @Override
    public void run() {
        RedisUtil.getRedisUtil().subscribeMsg(CacheManage.redisMsgSubListener, Constant.RedisPubSubKey.UPDATECACHE);
    }
}
