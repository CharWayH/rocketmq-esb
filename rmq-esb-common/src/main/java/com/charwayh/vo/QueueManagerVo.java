package com.charwayh.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.vo
 * @date:2023/5/28
 */
@Data
public class QueueManagerVo implements Serializable {
    public QueueManagerVo() {
    }

    public QueueManagerVo(Integer bscId, String qmName) {
        this.bscId = bscId;
        this.qmName = qmName;
    }

    public QueueManagerVo(Integer qmPid, Integer bscId, String qmName) {
        this.qmPid = qmPid;
        this.bscId = bscId;
        this.qmName = qmName;
    }

    public QueueManagerVo(Integer bscId, Integer qmPid, String qmName, Date qmCreateTime, Integer qmStatus) {
        this.qmName = qmName;
        this.qmPid = qmPid;
        this.bscId = bscId;
        this.qmCreateTime = qmCreateTime;
        this.qmStatus = qmStatus;
    }

    /**
     * 主键ID
     */
    private Integer qmId;

    /**
     * 父ID
     */
    private Integer qmPid;

    /**
     * 外键-业务系统ID
     */
    private Integer bscId;

    /**
     * 队列管理器
     */
    private String qmName;

    /**
     * 端口
     */
    private Integer qmPort;

    /**
     * 通道
     */
    private String qmChannel;

    /**
     * 字符集
     */
    private Integer qmCcsid;

    /**
     * 队列管理器IP地址
     */
    private String qmIp;

    /**
     * 创建时间
     */
    private Date qmCreateTime;

    /**
     * 是否可用（1-可用，2-不可用）新增且未测试的标记为不可用
     */
    private Integer qmStatus;

    /**
     * 连接模式，默认直连(0-直连，1-硬负载，2-软负载)
     */
    private Integer qmUseHardwareLoad;

    /**
     * 是否是主前置机，0：不是 1：是
     */
    private Integer qmIsMaster;

    /**
     *系统编号
     */
    private String sysCode;

    /**
     * 系统名称
     */
    private String sysName;


}
