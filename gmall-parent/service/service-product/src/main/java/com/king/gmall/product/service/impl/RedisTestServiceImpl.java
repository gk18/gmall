package com.king.gmall.product.service.impl;

import com.king.gmall.product.service.RedisTestService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/***
 * ClassName: RedisTestServiceImpl
 * Package: com.king.gmall.product.service.impl
 * @author GK
 * @date 2023/9/20 12:37
 * @description 分布式锁redis测试接口1
 * @version 1.0
 */
@Service
public class RedisTestServiceImpl implements RedisTestService {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;

    //使用redisson对redis数据进行加一操作
    @Override
    public void incr() {
        RLock lock = redissonClient.getLock("lock");
        try {
            if (lock.tryLock(100, 100, TimeUnit.SECONDS)) {
                Thread.currentThread().getId();
                Integer num = (Integer) redisTemplate.opsForValue().get("king");
                if (num != null) {
                    num++;
                    redisTemplate.opsForValue().set("king", num);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void incr1() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        if (redisTemplate.opsForValue().setIfAbsent("lock", uuid, 5, TimeUnit.SECONDS)) {
            try {
                Integer num = (Integer) redisTemplate.opsForValue().get("king");
                if (num != null) {

                    num++;
                    redisTemplate.opsForValue().set("king", num);
                }
                int i = 1 / 0;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //判断是否是本身的锁
//                String redisUUID = (String) redisTemplate.opsForValue().get("lock");
//                if(uuid.equals(redisUUID)) {
//                    redisTemplate.delete("lock");
//                }
                DefaultRedisScript<Integer> script = new DefaultRedisScript<>();
                script.setScriptText("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end");
                System.out.println(redisTemplate.execute(script, Arrays.asList("lock"), uuid));


            }
        } else {
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            incr1();
        }
    }
}
