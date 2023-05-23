package com.charwayh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author: create by CharwayH
 * @description: com.com.com.charwayh
 * @date:2023/5/22
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer8762 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer8762.class, args);
    }
}
