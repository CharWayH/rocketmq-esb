package com.charwayh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: create by CharwayH
 * @description: com.com.com.charwayh
 * @date:2023/5/22
 */
@SpringBootApplication
@EnableDiscoveryClient // 开启注册中心客户端（建议添加该注解）
public class RmqPubApp8080 {
    public static void main(String[] args) {
        SpringApplication.run(RmqPubApp8080.class, args);
    }
}
