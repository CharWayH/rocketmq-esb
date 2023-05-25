package com.charwayh.service;

import com.alibaba.fastjson.JSON;
import com.charwayh.annotation.MQLog;
import com.charwayh.entity.MessageResult;
import com.charwayh.util.RmqUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
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
    @MQLog
    public MessageResult sendMsg(Map map) {
        String producer = (String) map.get("producer");
        String consumer = (String) map.get("consumer");
        String topic = (String) map.get("topic");
        List list = (List) map.get("msg");
        Object o = list.get(0);
        JSON json = (JSON) JSON.toJSON(o);
        String msg = json.toJSONString();
        RmqUtils rmqUtils = RmqUtils.getInstance();
        return rmqUtils.sendMsg(producer,consumer, topic, msg);
    }
}
