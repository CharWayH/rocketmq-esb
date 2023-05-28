package com.charwayh.manage;

import com.charwayh.constant.Constant;
import com.charwayh.mapper.BusinessSystemConfMapper;
import com.charwayh.mapper.QueueManagerMapper;
import com.charwayh.mapper.QueueMapper;
import com.charwayh.thread.SubscribeMsgThread;
import com.charwayh.upon.domain.BusinessSystemConf;
import com.charwayh.upon.domain.Queue;
import com.charwayh.upon.domain.QueueManager;
import com.charwayh.util.DateUtil;
import com.charwayh.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.manage
 * @date:2023/5/27
 */
@Component
public class CacheManage {
    private static Logger logger = LoggerFactory.getLogger(CacheManage.class);

    @Autowired
    private QueueManagerMapper queueManagerMapper;

    @Autowired
    private QueueMapper queueMapper;

    @Autowired
    private BusinessManage businessManage;

    @Autowired
    private BusinessSystemConfMapper businessSystemConfMapper;

    /**
     * 系统编号和队列管理器对象MAP  系统编号<队列管理器ID,队列管理器对象>
     */
    public static Map<String, List<QueueManager>> sysCodeWithQueueManagerMap       = new ConcurrentHashMap<>();

    /**
     * 队列管理器ID和其绑定的系统CODE的MAP
     */
    public static Map<Integer, String> queueManageIdWithSysCodeMap      = new ConcurrentHashMap<>();

    /**
     * 队列管理器ID和其绑定的队列的MAP
     */
    public static Map<Integer, List<Queue>>       queueManageIdWithQueueMap        = new ConcurrentHashMap<>();

    /**
     * 系统编号和业务系统对象集合MAP
     */
    public static Map<String, BusinessSystemConf> sysCodeWithBusinessSystemConfMap = new ConcurrentHashMap<>();

    /**
     * 系统编号和该系统是否开启IP输入校验MAP
     */
    public static Map<String, Integer>            sysCodeWithIpCheckMap            = new ConcurrentHashMap<>();

    /**
     * 系统编号和该系统是否开启客户端消息缓存MAP
     */
    public static Map<String, Integer>            sysCodeWithClientMsgCacheMap     = new ConcurrentHashMap<>();

    /**
     * 系统编号和该系统是否开启服务端消息缓存MAP
     */
    public static Map<String, Integer>            sysCodeWithServiceMsgCacheMap    = new ConcurrentHashMap<>();

    /**
     * 系统编号和该系统是否开启当前运行环境校验缓存MAP
     */
    public static Map<String, Integer>            sysCodeWithRunEnvCheckCacheMap   = new ConcurrentHashMap<>();

    /**
     * 队列管理器集合
     */
    public static Set<String> qmSet = new HashSet<>();

    /**
     * 主题集合
     */
    public static Set<String> topicSet = new HashSet<>();

    /**
     * 当前运行环境标识值
     */
    public static String                          runningEnv;

    /**
     * 当前运行环境校验开关 1-开启  2-关闭
     */
    public static int                             runningEnvStatus;

    public static RedisMsgSubListener redisMsgSubListener = new RedisMsgSubListener();

    @PostConstruct
    public void init() {
        initCache();
    }

    /**
     * @Author SangYD
     * @Description 初始化加载配置信息
     * @Date 18:23 2019/4/18
     **/
    public void initCache() {
        logger.info("初始化加载缓存开始,{}", DateUtil.format(new Date(), Constant.DATE_FORMAT));
        cacheAllData();
        logger.info("初始化加载缓存结束,{}", DateUtil.format(new Date(), Constant.DATE_FORMAT));
        logger.info("注册redis的缓存更新订阅开始,{}", DateUtil.format(new Date(), Constant.DATE_FORMAT));
        new SubscribeMsgThread().start();
        logger.info("注册redis的缓存更新订阅结束,{}", DateUtil.format(new Date(), Constant.DATE_FORMAT));
    }


    public void cacheAllData() {
        // 运行时环境信息缓存
        cacheRunningEnvInfo();
        // 运行时环境状态信息缓存
        cacheRunningEnvStatus();
        // 系统信息与其他信息缓存
        cacheSysCodeWithOtherInfo();
        // 系统信息与队列管理器信息缓存
        cacheSysCodeWithQueueManager();
        // 队列管理器与队列信息缓存
        cacheQueueManageIdWithQueue();
//        cacheMasterQMWithQueueManager();
//        /***451新增**/
//        cacheServerNodeConf();
//        cacheMqGlobalConf();
        //注册钩子
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("钩子正在执行最后操作-------");
//                Map<String, MqListenerManage> topicUrlWithListenerMap = ListenerInfoManage.topicUrlWithListenerMap;
//                for (MqListenerManage mqListenerManage : topicUrlWithListenerMap.values()) {
//                    //停止线程
//                    mqListenerManage.stopListen();
//                    //断开订阅并记录日志
//                    String topicUrl  = mqListenerManage.getTopicUrl();
//                    String sessionId = ListenerInfoManage.getSessionId(topicUrl);
//                    if (StringUtils.isNotBlank(topicUrl) && StringUtils.isNotBlank(sessionId)) {
//                        webSocketTopicManage.UntyingTopic(sessionId, topicUrl);
//                    }
//                }
                //暂停防止日志 ack没有处理完毕
                try {
                    Thread.sleep(5000);
                    logger.info("钩子执行结束------------");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * @Author SangYD
     * @Description 更新redis的缓存信息
     * @Date 18:34 2020-11-18
     * @Return void
     **/
    public void cacheRunningEnvInfo() {
        cacheRunningEnvInfo(null, null);
    }


    /**
     * @Author SangYD
     * @Description 更新redis的缓存信息
     * @Date 18:34 2020-11-18
     * @Return void
     **/
    public synchronized void cacheRunningEnvInfo(final String oldEnv, final String newEnv) {
        logger.info("开始执行cacheRunningEnvInfo方法，更新存于mysql中的缓存信息");
        //设置当前运行环境标识值，默认为DEFAULT
        String currentEnv = StringUtils.isNotBlank(newEnv) ? newEnv : businessManage.getRunningEnvFlag();
        if (StringUtils.isBlank(currentEnv)) {
            runningEnv = Constant.DEFAULT;
        } else {
            runningEnv = currentEnv;
        }
        logger.info("cacheRunningEnvInfo方法执行完毕,当前运行环境标识调整为{}", runningEnv);
    }


    /**
     * @Author SangYD
     * @Description 更新redis的缓存信息
     * @Date 18:34 2020-11-18
     * @Return void
     **/
    public void cacheRunningEnvStatus() {
        cacheRunningEnvStatus(null);
    }

    /**
     * @Author SangYD
     * @Description 更新redis的缓存信息
     * @Date 18:34 2020-11-18
     * @Return void
     **/
    public synchronized void cacheRunningEnvStatus(Integer status) {
        logger.info("开始执行cacheRunningEnvStatus方法，更新存于redis中的缓存信息");
        //设置默认的运行环境校验开关
        if (status != null) {
            runningEnvStatus = status;
        } else if (!RedisUtil.getRedisUtil().exists(Constant.RedisCacheKey.RUNNINGENVSTATUS)) {
            runningEnvStatus = Constant.UseStatus.ON;
        } else {
            runningEnvStatus = Integer.valueOf(RedisUtil.getRedisUtil().get(Constant.RedisCacheKey.RUNNINGENVSTATUS));
        }
        logger.info("cacheRunningEnvStatus方法执行完毕,当前运行环境开关调整为{}", runningEnvStatus);
    }

    /**
     * @Author SangYD
     * @Description 查询系统配置表，缓存系统编号和是否开启连接池开关的关系
     * @Date 14:14 2019/4/17
     **/
    public synchronized void cacheSysCodeWithOtherInfo() {
        logger.info("开始执行cacheSysCodeWithOtherInfo方法，更新系统编号与其系统相关的信息缓存");
        //ip输入校验开关
        Map<String, Integer> sysCodeWithIpCheckMapCache = new HashMap<>();
        //客户端消息缓存
        Map<String, Integer> sysCodeWithClientMsgCacheMapCache = new HashMap<>();
        //服务端消息缓存
        Map<String, Integer> sysCodeWithServiceMsgCacheMapCache = new HashMap<>();
        //运行环境校验缓存
        Map<String, Integer> sysCodeWithRunEnvCheckMapCache = new HashMap<>();
        //系统配置信息
        Map<String, BusinessSystemConf> businessSystemConfMap  = new HashMap<>();
        List<BusinessSystemConf> businessSystemConfList = businessSystemConfMapper.getConfList();
        if (CollectionUtils.isNotEmpty(businessSystemConfList) && businessSystemConfList.size() > 0) {
            /** 遍历数据，放入缓存*/
            for (BusinessSystemConf businessSystemConf : businessSystemConfList) {
                if (StringUtils.isNotBlank(businessSystemConf.getBscCode())) {
                    String sysCode         = businessSystemConf.getBscCode();
                    int    ipCheck         = businessSystemConf.getBscIpCheck() != null ? businessSystemConf.getBscIpCheck() : Constant.Status.NORMAL;
                    int    clientMsgCache  = businessSystemConf.getBscClientMsgCache() != null ? businessSystemConf.getBscClientMsgCache() : Constant.Status.NORMAL;
                    int    serviceMsgCache = businessSystemConf.getBscServiceMsgCache() != null ? businessSystemConf.getBscServiceMsgCache() : Constant.Status.NORMAL;
                    int    runEnvCheck     = businessSystemConf.getBscRunEnvCheck() != null ? businessSystemConf.getBscRunEnvCheck() : Constant.Status.NORMAL;
                    sysCodeWithIpCheckMapCache.put(sysCode, ipCheck);
                    sysCodeWithClientMsgCacheMapCache.put(sysCode, clientMsgCache);
                    sysCodeWithServiceMsgCacheMapCache.put(sysCode, serviceMsgCache);
                    sysCodeWithRunEnvCheckMapCache.put(sysCode, runEnvCheck);
                    businessSystemConfMap.put(sysCode, businessSystemConf);
                }
            }
        } else {
            logger.info("获取系统配置信息时,当前mysql中未获取到配置相关信息,{}", DateUtil.format(new Date(), Constant.DATE_FORMAT));
        }
        sysCodeWithBusinessSystemConfMap = businessSystemConfMap;
        sysCodeWithIpCheckMap = sysCodeWithIpCheckMapCache;
        sysCodeWithClientMsgCacheMap = sysCodeWithClientMsgCacheMapCache;
        sysCodeWithServiceMsgCacheMap = sysCodeWithServiceMsgCacheMapCache;
        sysCodeWithRunEnvCheckCacheMap = sysCodeWithRunEnvCheckMapCache;
        logger.info("cacheSysCodeWithOtherInfo方法执行完毕");
    }

    /**
     * @Author SangYD
     * @Description 查询队列管理器配置表，缓存系统编号与队列管理器信息的关系
     * @Date 14:14 2019/4/17
     **/
    public synchronized void cacheSysCodeWithQueueManager() {
        logger.info("开始执行cacheSysCodeWithQueueManager，更新系统编号与其系统下的队列管理器的信息缓存");
        List<BusinessSystemConf>         businessSystemConfList            = businessSystemConfMapper.getConfList();//业务系统配置集合
        List<QueueManager>               baseQueueManagerList              = queueManagerMapper.getQueueManagerList();//队列管理器集合
        Map<String, List<QueueManager>>  sysCodeWithQueueManagerCacheMap   = new HashMap<>();
        Map<Integer, List<QueueManager>> queueManagerMap                   = new HashMap<>();
        Map<Integer, List<Integer>>      queueManagerIdWithSysCode         = new HashMap<>();
        Map<Integer, String>             queueManagerIdWithSysCodeCacheMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(baseQueueManagerList) && baseQueueManagerList.size() > 0) {
            /** 遍历队列管理器集合，建立 系统ID（主键）：队列列管理器集合的绑定关系*/
            for (QueueManager queueManager : baseQueueManagerList) {
                int sysId = queueManager.getBscId();
                /**sysId和队列管理器集合的map*/
                List<QueueManager> mid = queueManagerMap.containsKey(sysId) ? queueManagerMap.get(sysId) : new ArrayList<>();
                mid.add(queueManager);
                queueManagerMap.put(sysId, mid);
                /**sysId和队列管理器id集合的map*/
                List<Integer> qmId = queueManagerIdWithSysCode.containsKey(sysId) ? queueManagerIdWithSysCode.get(sysId) : new ArrayList<>();
                qmId.add(queueManager.getQmId());
                queueManagerIdWithSysCode.put(sysId, qmId);
                // 存入qm信息到qmSet中 供鉴权时使用
                qmSet.add(queueManager.getQmName());
            }
            if (CollectionUtils.isNotEmpty(businessSystemConfList) && businessSystemConfList.size() > 0) {
                /**遍历业务系统配置信息，将系统ID（主键）的关系转移到系统编号*/
                for (BusinessSystemConf businessSystemConf : businessSystemConfList) {
                    String sysCode = businessSystemConf.getBscCode();
                    int    sysId   = businessSystemConf.getBscId();
                    if (queueManagerMap.containsKey(sysId)) {
                        List<QueueManager> mid = queueManagerMap.get(sysId);
                        sysCodeWithQueueManagerCacheMap.put(sysCode, mid);
                    }
                    if (queueManagerIdWithSysCode.containsKey(sysId)) {
                        List<Integer> qmId = queueManagerIdWithSysCode.get(sysId);
                        for (Integer qm_id : qmId) {
                            queueManagerIdWithSysCodeCacheMap.put(qm_id, sysCode);
                        }
                    }
                }
            } else {
                logger.info("获取系统配置信息时,当前mysql中未获取到配置相关信息,{}", DateUtil.format(new Date(), Constant.DATE_FORMAT));
            }
        } else {
            logger.info("获取队列管理器配置信息时,当前mysql中未获取到配置相关信息,{}", DateUtil.format(new Date(), Constant.DATE_FORMAT));
        }

        sysCodeWithQueueManagerMap = sysCodeWithQueueManagerCacheMap;
        queueManageIdWithSysCodeMap = queueManagerIdWithSysCodeCacheMap;
        logger.info("cacheSysCodeWithQueueManager方法执行完毕");
    }


    /**
     * @Author SangYD
     * @Description 查询队列表，缓存队列管理器编号与队列的关系
     * @Date 14:14 2019/4/17
     **/
    public synchronized void cacheQueueManageIdWithQueue() {
        logger.info("cacheQueueManageIdWithQueue，更新队列管理器与其下的队列集合的信息缓存");
        Map<Integer, List<Queue>> cacheMap  = new HashMap<>();
        List<Queue>               queueList = queueMapper.getQueueList();
        if (CollectionUtils.isNotEmpty(queueList) && queueList.size() > 0) {
            for (Queue queue : queueList) {
                int         queueManagerId = queue.getQmId();
                List<Queue> cacheQueueList = cacheMap.containsKey(queueManagerId) ? cacheMap.get(queueManagerId) : new ArrayList<>();
                cacheQueueList.add(queue);
                // TODO 新增主题集合的添加
                cacheMap.put(queueManagerId, cacheQueueList);
            }
        } else {
            logger.info("获取队列信息时,当前mysql中未获取到相关信息,{}", DateUtil.format(new Date(), Constant.DATE_FORMAT));
        }
        cacheMap.forEach((key, value) -> {
            List<Queue> queues = value.stream().sorted(Comparator.comparing(Queue::getQPutStatus, Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(Queue::getQGetStatus, Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(Queue::getQCollectStatus, Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(Queue::getQNameGet, Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());
            value.clear();
            value.addAll(queues);
        });
        queueManageIdWithQueueMap = cacheMap;
        logger.info("cacheQueueManageIdWithQueue方法执行完毕");
    }

}
