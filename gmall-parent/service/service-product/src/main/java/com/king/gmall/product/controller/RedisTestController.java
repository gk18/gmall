package com.king.gmall.product.controller;

import com.king.gmall.common.result.Result;
import com.king.gmall.product.service.RedisTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/***
 * ClassName: RedisTestController
 * Package: com.king.gmall.product.controller
 * @author GK
 * @date 2023/9/20 12:40
 * @description 测试分布式锁控制层
 * @version 1.0
 */
@RestController
@RequestMapping
public class RedisTestController {
    @Resource
    private RedisTestService redisTestService;
    @GetMapping("/redis/product/incr")
    public Result incr() {
        redisTestService.incr();
        return Result.ok();
    }
}
