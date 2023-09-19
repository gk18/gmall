package com.king.gmall.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/***
 * ClassName: GmallThreadPoolConfig
 * Package: com.king.gmall.common.config
 * @author GK
 * @date 2023/9/19 21:21
 * @description gmall线程池配置类
 * @version 1.0
 */
@Configuration
public class GmallThreadPoolConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(8,8,0, TimeUnit.SECONDS,new LinkedBlockingQueue<>(10000));
    }
}
