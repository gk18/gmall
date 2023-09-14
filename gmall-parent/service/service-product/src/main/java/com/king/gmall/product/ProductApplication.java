package com.king.gmall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/***
 * ClassName: ProductApplication
 * Package: com.king.gmall.product
 *  商品管理微服务的启动类
 * @author GK
 * @version 1.0
 * @date 2023/9/14 19:03
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.king.gmall")
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,args);
    }
}
