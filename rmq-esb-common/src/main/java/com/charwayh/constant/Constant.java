package com.charwayh.constant;



/**
 * Description: 常量类
 * Author: SangYD
 * Date: 2019-11-26 15:54
 * Copyright (c) 2019, ewell.com
 * All Rights Reserved.
 */
public interface Constant {
    /**
     * 基本参数类型
     */
    String[] BASE_PARAMS_TYPE      = new String[]{"String", "Integer", "Boolean", "Long", "Short", "Double", "Float", "Byte", "Character"};
    /**
     * 判断数据通道是否完整的标志
     */
    String   POINT                 = ".";
    /**
     * 英文逗号分隔符
     */
    String   COMMA                 = ",";
    /**
     * 下划线分隔符
     */
    String   UNDERLINE             = "_";
    /**
     * 拼接分隔符
     */
    String   JOIN_FLAG             = "#";
    /**
     * 拼接分隔符
     */
    String   JOIN_FLAG_AT          = "@";
    /**
     * 消息ID
     */
    String   MESSAGE_ID            = "messageId";
    /**
     * 消息内容
     */
    String   MESSAGE               = "message";
    /**
     * 将数据通道转化为PUT队列名称时的队列类型
     */
    String   TYPE_PUT              = "0";
    /**
     * 将数据通道转化为GET队列名称时的队列类型
     */
    String   TYPE_GET              = "1";
    /**
     * 通用时间format格式
     */
    String   DATE_FORMAT           = "yyyy-MM-dd HH:mm:ss";
    /**
     * 队列管理器通用编码格式
     */
    String   QUEUEMANAGEER_CHARSET = "1386";
    /**
     * 默认值
     */
    String   DEFAULT               = "DEFAULT";
    /**
     * push
     */
    String PUBSUB = "QMGR.P";
    Integer LISTEN=2;

    /**
     * 负载模式
     */
    interface HardwareLoad {
        /***
         * 0-直连（默认为直连），1-硬负载模式，2-软负载模式
         */
        int DIRCTCONNECT = 0;
        /**
         * 1-硬负载模式
         */
        int HARDLOAD     = 1;
        /**
         * 2-软负载模式
         */
        int SOFTLOAD     = 2;

    }
    /**
     * @Author SangYD
     * @Description 操作区域
     * @Date 17:40 2019/4/17
     **/
    interface MqMode {
        /**
         *直连
         */
        Integer DIRECT  = 0;
        /**
         * 负载
         */
        Integer LOAD = 1;
        /**
         * 禁用
         */
        Integer DISABLE = 2;
    }

    /**
     * @Author SangYD
     * @Description redis发布订阅功能相关关键字
     * @Date 13:31 2019/12/9
     * @Return
     **/
    interface RedisPubSubKey {
        /**
         * 更新配置数据缓存
         */
        String UPDATECACHE              = "updateCache";
        /**
         * 停止队列监听
         */
        String UNSUBSCRIBETOPIC         = "unsubscribeTopic";
        /**
         * 重发滞留数据
         */
        String RESENDMESSAGES           = "resendMessages";
        /**
         * 根据条件重发滞留数据
         */
        String RESENDMESSAGEBYCONDITION = "resendMessageByCondition";
        /**
         * 发送指令
         */
        String SENDINSTRUCTION          = "sendInstruction";
        /**
         * 运行环境同步
         */
        String RUNNINGENVSYNC           = "running_env_sync";
    }

    /**
     * @Author SangYD
     * @Description 日志步骤常量
     * @Date 18:28 2019/4/29
     **/
    interface LogStep {
        /**
         * 连接
         */
        int CONNECT    = 0;
        /**
         * 断连
         */
        int DISCONNECT = -1;
        /**
         * 放消息
         */
        int PUT        = 1;
        /**
         * 取消息
         */
        int GET        = 2;
        /**
         * 根据ID放消息
         */
        int PUTWITHID  = 1;
        /**
         * 根据ID取消息
         */
        int GETWITHID  = 2;
        /**
         * 监听获取数据
         */
        int LISTENER   = 3;
        /*
         *第一部分日志
         */
        int FIRSTPART  = 1;
        /*
         *第二部分日志
         */
        int SENCODPART = 2;
    }

    /**
     * @Author SangYD
     * @Description 日志操作类型
     * @Date 16:16 2019/4/26
     * @Return
     **/
    interface OperationType {
        /**
         * 连接
         */
        String C = "1";
        /**
         * 放消息
         */
        String P = "2";
        /**
         * 取消息
         */
        String G = "3";
        /**
         * 断连
         */
        String D = "4";
        /**
         * 监听
         */
        String L = "5";

        /**
         * WS协议
         */
        String WS = "1";

        /**
         * RS协议
         */
        String RS = "2";

        /**
         * 业务类型
         */
        String BUSINESS = "10";
    }

    /**
     * @Author SangYD
     * @Description MQ操作
     * @Date 16:35 2019/4/17
     **/
    interface MqOperation {
        /**
         * 连接队列管理器
         */
        String CONNECT    = "connect";
        /**
         * 往队列放置消息
         */
        String PUTMSG     = "putMsg";
        /**
         * 从队列获取消息
         */
        String GETMSG     = "getMsg";
        /**
         * 从队列浏览消息
         */
        String BROWSEMSG  = "browseMsg";
        /**
         * 断开队列管理器连接
         */
        String DISCONNECT = "disconnect";
    }

    /**
     * @Author SangYD
     * @Description 开关/ 启/禁用状态
     * @Date 17:40 2019/4/17
     **/
    interface UseStatus {
        /**
         * 开
         */
        int ON  = 1;
        /**
         * 关闭
         */
        int OFF = 2;
    }

    /**
     * @Author SangYD
     * @Description 状态
     * @Date 17:40 2019/4/17
     **/
    interface Status {
        /**
         * 正常
         */
        int NORMAL   = 1;
        /**
         * 异常
         */
        int ABNORMAL = 2;
    }

    interface RedisCacheKey {
        /******************************************KEY前缀管理****************************************/
        /**
         * redis中记录的日志数据分包
         */
        String PREFIX       = "cw:rmq-esb:";
        /**
         * SDK业务操作日志
         */
        String SDK_STEP_LOG = PREFIX + "log:sdkStepLog";
        /**
         * SDK ACK操作日志
         */
        String SDK_ACK_LOG  = PREFIX + "log:sdkAckLog";
        /**
         * 队列资源使用情况关键字
         */
        String QUEUE_USAGE  = PREFIX + "log:queueUsageLog";

        /**
         * 应用的缓存
         */
        String SYSCODE_CACHE = PREFIX + "listenLimit:cache-";


        /******************************************请求响应相关KEY管理****************************************/

        /**
         * 客户端连接基础信息的绑定(QueueConfig,QueueManager,SysCode,FlowControl,ip)
         */
        String CIDCACHEINFO                = PREFIX + "cache:cidCacheInfo-";
        /**
         * 客户端连接唯一标识和队列资源组使用对象的绑定
         */
        String CIDWITHUSEQUEUES            = PREFIX + "cache:cidWithUseQueues-";
        /**
         * 队列管理器主键和队列池使用到的索引的绑定
         */
        String QUEUEMANAGEIDWITHQUEUEINDEX = PREFIX + "info:queueManageIdWithQueueIndex-";
        /**
         * 当前系统允许通行的环境标识值
         */
        //String RUNNINGENVFLAG              = PREFIX + "info:runningEnvFlag";
        /**
         * 运行环境是否开启校验开关
         */
        String RUNNINGENVSTATUS              = PREFIX + "info:runningEnvStatus";

        /******************************************发布订阅相关KEY管理****************************************/
        /**
         * TOPIC和订阅该话题的缓存信息
         */
        String LISTENERCACHEINFO  = PREFIX + "listen:ListenerCacheInfo-";
        /**
         * 订阅话题和是否被订阅的状态的关系
         */
        //String TOPICURLWITHSTATUS = PREFIX + "listen:topicUrlWithStatus-";
        /**
         * 会话ID和其下关联的订阅话题的绑定
         */
        String SESSIONIDWITHTOPIC = PREFIX + "listen:sessionIdWithTopic-";
        /**
         * 其下关联的订阅话题和会话ID的绑定
         */
        String TOPICWITHSESSIONID = PREFIX + "listen:topicWithSessionId-";
        /**
         * 等待ACK的数据
         */
        String WAITINGDATA        = PREFIX + "data:waitingData-";
        /**
         * 用于监听失效后滞留数据的清理KEY
         */
        String WAITINGDATACLEAN       = PREFIX + "data:waitingDataClean-";
        /**
         * 等待过期的数据
         */
        String BAKDATAEXPIREDKEY  = PREFIX + "data:expiredKey-";

        String QUEUE_DEPTH_KEY = PREFIX + "queue-depth:";

        /**
         * 回声测试redis存储的数据
         */
        String ECHO_TEST_MESSAGES = PREFIX + "echoTest:messages-";
        /*
        *订阅话题及对应线程状态
         */
        String TOPICWITHTHREADSTATUS = PREFIX + "listen:topicWithThreadStatus-";
        /****************SDK451新增缓存，SDK服务器节点配置相关缓存信息****************/
        /**
         *开启校验开关
         */
        String SDKNODEFLAG              = PREFIX + "info:sdkNodeFlag";
        /**
         * Mq全局配置
         */
        String MQGLOBALCONF            = PREFIX + "info:mqGlobalConf-";
        /**
         * SDK服务器节点配置
         */
        String SDKNODECONF            = PREFIX + "info:mqServerNodeConf-";

    }

    interface LoadRedisKey {
        String CURRENT_MQ_SERVER_KEY           = "ewell:sdk:CURRENT_MQ_SERVER";
        String QMGR_SERVER_KEY                 = "ewell:sdk:QMGR_MQ_SERVER";
        String PUBSUB_SERVER_CHANGE_LOCK_KEY   = "ewell:sdk:PUBSUB_SERVER_CHANGE_LOCK";
        String PUBSUB_SERVER_CHANGE_UNLOCK_KEY = "ewell:sdk:PUBSUB_SERVER_CHANGE_UNLOCK";
    }


    interface AgreementConfig {
        /**
         * 协议配置的webservice模式
         */
        String  AGREEMENT_MODE_WS         = "4";
        /**
         * 协议配置的restful模式
         */
        String  AGREEMENT_MODE_RS         = "5";
        /**
         * 协议配置参数类型 -- 入参
         */
        Integer AGREEMENT_PARAMS_TYPE_IN  = 1;
        /**
         * 协议配置参数类型 -- 出参
         */
        Integer AGREEMENT_PARAMS_TYPE_OUT = 2;
        /**
         * soapUi方式解析wsdl
         */
        Integer ANALYSIS_WAY_SOAPUI       = 1;
        /**
         * dom4j方式解析wsdl
         */
        Integer ANALYSIS_WAY_DOM4J        = 2;
    }

    interface PublicTopicKey {
        /**
         * 公有TOPIC
         */
        String PUBLIC_TOPIC            = "/topic/public";
        /**
         * 报道指令码
         */
        String CMD_JOIN_FLAG           = ":";
        /**
         * 报道指令码
         */
        int    CMD_ID_CHECK            = 1;
        /**
         * 报道指令
         */
        String CMD_CHECK               = "CHECK";
        /**
         * 停止指令码
         */
        int    CMD_ID_STOP             = 2;
        /**
         * 停止指令
         */
        String CMD_STOP                = "STOP_SUB";
        /**
         * 启动指令码
         */
        int    CMD_ID_START            = 3;
        /**
         * 启动指令
         */
        String CMD_START               = "START_SUB";
        /**
         * 重启指令码
         */
        int    CMD_ID_RESTART          = 4;
        /**
         * 重启指令
         */
        String CMD_RESTART             = "RESTART_SUB";
        /**
         * 清理指令码
         */
        int    CMD_ID_CLEAN            = 5;
        /**
         * 运行环境同步指令
         */
        String CMD_RUNNING_ENV_SYNC    = "RUNNING_ENV_SYNC";
        /**
         * 运行环境同步指令码
         */
        int    CMD_ID_RUNNING_ENV_SYNC = 8;
        /**
         * 回声测试指令
         */
        int    CMD_ID_ECHOTEST         = 6;
        /**
         * 回声测试指令
         */
        String CMD_ECHOTEST            = "ECHOTEST_SUB";
        /**
         * 侦听重启
         */
        int    CMD_ID_STARTALL         = 7;
        /**
         * 重启指令
         */
        String CMD_STARTALL            = "STARTALL_SUB";
        /**
         * 开启客户端缓存
         */
        int    CMD_ID_ISCLIENTCACHE    = 9;
        /**
         * 开启客户端缓存指令
         */
        String CMD_ISCLIENTCACHE       = "ISCLIENTCACH_SUB";
        /**
         * 关闭客户端缓存
         */
        int    CMD_ID_NOCLIENTCACHE    = 10;
        /**
         * 开启客户端缓存指令
         */
        String CMD_NOCLIENTCACHE       = "NOCLIENTCACH_SUB";
        /**
         * 切换运行环境
         */
        int    CMD_ID_CHANGERUNNINGENV = 11;
        /**
         * 切换运行环境指令
         */
        String CMD_CHANGERUNNINGENV    = "CHANGE_RUNNING_ENV";

        /**
         * 现在服务端运行环境
         */
        int    CMD_ID_SERVER_RUNNING_ENV_NOW = 12;
        /**
         * 现在服务端运行环境
         */
        String CMD_SERVER_RUNNING_ENV_NOW   = "SERVER_RUNNING_ENV_NOW_SUB";
    }

    interface SdkVersion {
        int OLD_VERSION = 1;
        int NEW_VERSION = 2;
    }

    interface DateType {
        String TODAY      = "0";
        String NEAR7DAYS  = "-7";
        String NEAR30DAYS = "-30";
    }

    interface StatisticalType {
        String CONNECT    = "CONNECT";
        String PUT        = "PUT";
        String GET        = "GET";
        String DISCONNECT = "DISCONNECT";
        String BUSINESS   = "BUSINESS";
    }

    interface AggType {
        //某天的24小时
        String DAY         = "DAY";
        //某小时的60分钟
        String HOUR        = "HOUR";
        //某天的1440分中
        String HOUR_MINUTE = "HOUR_MINUTE";
        //某分钟的60秒
        String MINUTE      = "MINUTE";
        //某秒的1000毫秒
        String SECOND      = "SECOND";
    }

    interface StatusCode {
        /**
         * 请求成功码
         */
        int SUCCESS = 200;
    }

    interface HttpMethodType {
        String[] TYPES   = new String[]{"GET", "POST", "DELETE", "OPTIONS", "HEAD", "PUT", "PATCH"};
        String   POST    = "POST";
        String   GET     = "GET";
        String   PUT     = "PUT";
        String   DELETE  = "DELETE";
        String   PATCH   = "PATCH";
        String   OPTIONS = "OPTIONS";
        String   HEAD    = "HEAD";
    }
}
