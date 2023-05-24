package com.charwayh;

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
 * @description: com.charwayh
 * @date:2023/5/22
 */
public class PubTest1 {
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        long begin = System.currentTimeMillis();
        DefaultMQProducer producer = new DefaultMQProducer("producer_grp_01");
        // 指定nameserver地址
        producer.setNamesrvAddr("192.168.1.182:9876");
            producer.start();
            for (int i = 0; i < 50; i++) {
                producer.setSendMsgTimeout(10000000);
                SendResult result = producer.send(new Message("PS10001", ("hello"+i).getBytes(RemotingHelper.DEFAULT_CHARSET)));
                System.out.println("发送完成");
                System.out.println(result.getSendStatus());
            }
            producer.shutdown();

        long end = System.currentTimeMillis();
        System.out.println((end-begin)/1000);
    }
}
