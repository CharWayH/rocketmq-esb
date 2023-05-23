package com.charwayh.service;

import com.charwayh.util.RmqUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.service
 * @date:2023/5/23
 */
@Service
public class PubService {


    /**
     * 推送消息
     */
    public void sendMsg(Map map){
        String producerGroup = (String) map.get("producerGroup");
        String topic = (String) map.get("topic");
        String msg = (String) map.get("msg");
        RmqUtils instance = RmqUtils.getInstance();
        instance.sendMsg(producerGroup, topic, msg);
    }
}
