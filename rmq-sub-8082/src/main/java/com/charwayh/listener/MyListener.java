package com.charwayh.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author: create by CharwayH
 * @description: com.charwayh.listener
 * @date:2023/5/22
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "PS10001", consumerGroup = "P03")
public class MyListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        log.info(s);
    }
}
