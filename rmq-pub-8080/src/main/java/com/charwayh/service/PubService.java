package com.charwayh.service;

import com.alibaba.fastjson.JSON;
import com.charwayh.annotation.MQLog;
import com.charwayh.constant.MessageConstant;
import com.charwayh.entity.MessageResult;
import com.charwayh.bussiness.RmqService;
import com.charwayh.entity.Result;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RmqService rmqService;

    /**
     * 推送消息
     */
    public Result sendMsg(Map map) {
        String producer = (String) map.get("producer");
        String consumer = (String) map.get("consumer");
        String topic = (String) map.get("topic");
        List list = (List) map.get("msg");
        Object o = list.get(0);
        JSON json = (JSON) JSON.toJSON(o);
        String msg = json.toJSONString();
        MessageResult messageResult = null;

        messageResult = rmqService.sendMsg(producer, consumer, topic, msg);
        if(MessageConstant.SEND_OK.name().equals(messageResult.getResult())) {
            return new Result(true, MessageConstant.SEND_OK.toString(), messageResult);
        }else{
            return new Result(false, messageResult.getResult());
        }
    }
}
