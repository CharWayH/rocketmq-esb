package com.charwayh.util;

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

    public void sendMsg(String producerGroup, String topic, String msg) {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        // 指定nameserver地址
        producer.setNamesrvAddr("192.168.1.182:9876");
        try {
            producer.start();
            producer.setSendMsgTimeout(10000000);
            SendResult result = producer.send(new Message(topic, msg.getBytes(RemotingHelper.DEFAULT_CHARSET)));
            System.out.println("发送完成");
            System.out.println(result);
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
    }
}
