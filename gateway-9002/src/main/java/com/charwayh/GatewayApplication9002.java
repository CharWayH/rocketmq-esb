package com.charwayh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: create by CharwayH
 * @description: com.charwayh
 * @date:2023/5/26
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication9002 {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication9002.class, args);
    }
}
