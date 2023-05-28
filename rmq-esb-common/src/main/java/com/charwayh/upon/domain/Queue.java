package com.charwayh.upon.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: create by CharwayH
 * @description: 队列表实体类
 * @date:2023/5/28
 */
@Data
@Table(name = "sdkQueue")
public class Queue implements Serializable {

    public Queue() {
    }

    public Queue(Integer qmId, Date qCreateTime) {
        this.qmId = qmId;
        this.qCreateTime = qCreateTime;
    }

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer qId;

    /**
     * 外键-队列管理器表ID
     */
    private Integer qmId;

    /**
     * put队列名称
     */
    private String qNamePut;

    /**
     * get队列名称
     */
    private String qNameGet;

    /**
     * collect队列名称
     */
    private String qNameCollect;

    /**
     * 创建时间
     */
    private Date qCreateTime;

    /**
     * get队列是否可用（1-可用，2-不可用）
     */
    private Integer qGetStatus;

    /**
     * put队列是否可用（1-可用，2-不可用）
     */
    private Integer qPutStatus;

    /**
     * collect队列是否可用（1-可用，2-不可用）
     */
    private Integer qCollectStatus;

    /**
     * 是否加入所属队列管理器的资源池 （1-加入，2-不加入）默认不加入
     */
    private Integer qJoinConnectPool;


    /**
     * 从 qNameGet 、qNamePut 、qNameCollect中截取出通道名称
     *
     * @return
     */
    public String fetchChannel() {
        String channel = "";
        String[] candidates = new String[]{this.qNamePut, this.qNameGet, this.qNameCollect};
        int length = candidates.length;
        for (int i = 0; i < length; i++) {
            String candidate = candidates[i];
            if (candidate != null) {
                String[] split = candidate.split("\\.");
                if (split.length == 4) {
                    channel = split[2];
                    if (channel != null && !"".equals(channel)) {
                        break;
                    }
                }
            }
        }
        return channel;
    }

}
