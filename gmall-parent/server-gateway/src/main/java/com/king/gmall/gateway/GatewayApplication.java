package com.king.gmall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/***
 * ClassName: GatewayApplication
 * Package: com.king.gmall.gateway
 * @author GK
 * @date 2023/9/15 20:27
 * @description 网关微服务启动类
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
