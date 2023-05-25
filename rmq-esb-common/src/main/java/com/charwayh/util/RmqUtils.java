package com.charwayh.util;

import com.charwayh.entity.MessageResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.com.charwayh.util
 * @date:2023/5/23
 */
public class RmqUtils {

    private RmqUtils() {
    }

    private final static RmqUtils rmqUtils = new RmqUtils();

    public static RmqUtils getInstance() {
        return rmqUtils;
    }

    /**
     * 发送消息
     * @param producerGroup
     * @param topic
     * @param msg
     */
    public MessageResult sendMsg(String producerGroup, String consumer, String topic, String msg) {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        // 指定nameserver地址
        producer.setNamesrvAddr("192.168.1.182:9876");
        MessageResult messageResult = new MessageResult();
        try {
            producer.start();
            producer.setSendMsgTimeout(10000000);
            SendResult sendResult = producer.send(new Message(topic, msg.getBytes(RemotingHelper.DEFAULT_CHARSET)));
            messageResult.setMessageId(sendResult.getMsgId());
            messageResult.setResult(sendResult.getSendStatus().toString());
            messageResult.setConsumer(consumer);
            messageResult.setMessageContent(msg);
            messageResult.setTopic(topic);
        } catch (
                MQClientException e) {
            e.printStackTrace();
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
        } finally {
            producer.shutdown();
        }
        return messageResult;
    }
}
