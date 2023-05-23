package com.charwayh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: create by CharwayH
 * @description: com.com.com.charwayh
 * @date:2023/5/22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RmqPubApp8080.class)
public class RmqPubApp8080Test {
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
//    @Test
//    public void testSendMsg() {
//        rocketMQTemplate.convertAndSend("PS10001","hello");
//    }
}
