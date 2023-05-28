package com.charwayh.upon.domain;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.upon.domain
 * @date:2023/5/28
 */

import com.charwayh.vo.QueueManagerVo;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Description: //队列管理器表实体类
 * Author: WangDL
 * Date: 2019-11-27 17:00
 */
@Data
@Table(name = "sdk_queue_manager")
public class QueueManager implements Serializable {
    public QueueManager() {
    }
    public QueueManager(QueueManager qm) {
        this.qmName = qm.getQmName ();
        this.qmPid = qm.getQmPid ();
        this.bscId = qm.bscId;
        this.qmCreateTime = qm.getQmCreateTime ();
        this.qmStatus = qm.getQmStatus ();
        this.qmUseHardwareLoad=qm.getQmUseHardwareLoad ();
        this.qmCreateTime=qm.getQmCreateTime ();
        this.qmId=qm.getQmId ();
        this.qmIp=qm.getQmIp ();
    }
    public QueueManager(QueueManagerVo qm) {
        this.qmName = qm.getQmName ();
        this.qmPid = qm.getQmPid ();
        this.bscId = qm.getBscId();
        this.qmCreateTime = qm.getQmCreateTime ();
        this.qmStatus = qm.getQmStatus ();
        this.qmUseHardwareLoad=qm.getQmUseHardwareLoad ();
        this.qmCreateTime=qm.getQmCreateTime ();
        this.qmId=qm.getQmId ();
        this.qmIp=qm.getQmIp ();
    }

    public QueueManager(Integer bscId, String qmName) {
        this.bscId = bscId;
        this.qmName = qmName;
    }

    public QueueManager(Integer qmPid, Integer bscId, String qmName) {
        this.qmPid = qmPid;
        this.bscId = bscId;
        this.qmName = qmName;
    }

    public QueueManager(Integer bscId, Integer qmPid, String qmName, Date qmCreateTime, Integer qmStatus) {
        this.qmName = qmName;
        this.qmPid = qmPid;
        this.bscId = bscId;
        this.qmCreateTime = qmCreateTime;
        this.qmStatus = qmStatus;
    }

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * 负载模式，默认直连(0-直连，1-硬负载，2-软负载)
     */
    private Integer qmUseHardwareLoad;

}
