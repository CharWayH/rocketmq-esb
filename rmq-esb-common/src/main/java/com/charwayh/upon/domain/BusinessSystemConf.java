package com.charwayh.upon.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Description: 业务系统配置信息表实体类
 * Author: WangDL
 * Date: 2019-09-17 14:20
 * Copyright (c) 2019, ewell.com
 * All Rights Reserved.
 */
@Data
@Table(name = "sdk_business_system_conf")
public class BusinessSystemConf  implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bscId;

    /**
     * 系统名称
     */
    private String bscName;

    /**
     * 系统编号
     */
    private String bscCode;

    /**
     * 所属厂商
     */
    private String bscManufactor;

//    /**
//     * 连接池开关 1开启，2不开启，默认为1
//     */
//    private Integer bscConnectPool;
//
//    /**
//     * 重连次数 默认为3次,-1表示一直重连，0表示不重连
//     */
//    private Integer bscReconnectNum;
//
//    /**
//     * 重连接时间
//     */
//    private Integer bscReconnectTime;
//
//    /**
//     * 数据分流开关 1开启，2不开启
//     */
//    private Integer bscSplitFlow;

    /**
     * 系统标识码 	新增有系统生成，确保唯一性
     */
    private String bscKey;

    /**
     * 资源告警阈值
     */
    private Double bscResourcePoolWarnning;

    /**
     * 创建时间
     */
    private Date bscCreateTime;

    /**
     * 备注
     */
    private String bscRemarks;

    /**
     * 客户端IP地址（多组之间，用,分隔）
     */
    private String bscClientIp;

    /**
     * 客户端ip输入开关（1开启，2不开启）
     */
    private Integer bscIpCheck;
    /**
     * 客户端消息缓存开关（1开启，2不开启）
     */
    private Integer bscClientMsgCache;

    /**
     * 服务端消息开关（1开启，2不开启）
     */
    private Integer bscServiceMsgCache;

    /**
     * 客户端ip输入开关（1开启，2不开启）
     */
    private Integer bscRunEnvCheck;

    /**
     * 单个队列可监听数量上限
     */
    private Integer bscListenNumLimit;

    /**
     * 系统流量设置
     */
    private Integer bscSysQpsConf;

}
