package com.charwayh.manage;

import com.charwayh.mapper.EnvFlagMapper;
import com.charwayh.upon.domain.EnvFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: create by CharwayH
 * @description:  业务操作管理类
 * @date:2023/5/27
 */
@Component
public class BusinessManage {


    @Autowired
    private EnvFlagMapper envFlagMapper;

    /**
     * @Author SangYD
     * @Description 获取当前的服务端运行环境标识值
     * @Date 9:33 2021/2/24
     * @Return String 标识值字符串
     **/
    public synchronized String getRunningEnvFlag() {
        List<EnvFlag> envFlagList = envFlagMapper.selectAll();
        if (envFlagList.size() > 0) {
            EnvFlag envFlag = envFlagList.get(0);
            return envFlag.getEnvFlag();
        } else {
            return null;
        }
    }
}
