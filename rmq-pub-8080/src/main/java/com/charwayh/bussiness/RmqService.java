package com.charwayh.bussiness;

import com.charwayh.annotation.MQLog;
import com.charwayh.constant.MessageConstant;
import com.charwayh.entity.MessageResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.com.charwayh.util
 * @date:2023/5/23
 */
@Component
public class RmqService {

//    private RmqService() {
//    }
//
//    private final static RmqService RMQ_SERVICE = new RmqService();
//
//    public static RmqService getInstance() {
//        return RMQ_SERVICE;
//    }
    @Value("${rocketmq.name-server}")
    private String RmqAddr;

    /**
     * 发送消息
     * @param producer
     * @param topic
     * @param msg
     */
    @MQLog
    public MessageResult sendMsg(String producer, String consumer, String topic, String msg){
        DefaultMQProducer mqProducer = new DefaultMQProducer(producer);
        // 指定nameserver地址
        mqProducer.setNamesrvAddr(RmqAddr);
        MessageResult messageResult = new MessageResult();
        try {
            mqProducer.start();
            mqProducer.setSendMsgTimeout(10000000);
            SendResult sendResult = mqProducer.send(new Message(topic, msg.getBytes(RemotingHelper.DEFAULT_CHARSET)));

            messageResult.setMessageId(sendResult.getMsgId());
            messageResult.setResult(sendResult.getSendStatus().toString());
        } catch (
                RemotingException e) {
            e.printStackTrace();
        } catch (
                MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (
                UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MQClientException e) {
            e.printStackTrace();
            messageResult.setResult(MessageConstant.CONNECT_UNSUCCESS.toString());
            return messageResult;
        } finally {
            mqProducer.shutdown();
        }
        return messageResult;
    }
}
