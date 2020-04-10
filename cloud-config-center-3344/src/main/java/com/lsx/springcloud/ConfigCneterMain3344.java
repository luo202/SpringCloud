package com.lsx.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableConfigServer
public class ConfigCneterMain3344 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigCneterMain3344.class,args);
    }
}
