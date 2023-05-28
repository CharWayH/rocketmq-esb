package com.charwayh.upon.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.upon.domain
 * @date:2023/5/27
 */
@Data
@Table(name = "sdk_env_flag")
public class EnvFlag implements Serializable {

    public EnvFlag() {
        super();
    }

    public EnvFlag(String envFlag) {
        this.envFlag = envFlag;
    }

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 环境标识
     */
    private String envFlag;
    /**
     * 系统名称
     */
    private String envFlagOld;
    /**
     * 环境标识更新时间
     */
    private Long envUpdateTime;
}

