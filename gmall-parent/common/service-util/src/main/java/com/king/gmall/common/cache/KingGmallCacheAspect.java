package com.king.gmall.common.cache;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/***
 * ClassName: KingGmallCacheAspect
 * Package: com.king.gmall.common.cache
 * @author GK
 * @date 2023/9/20 19:53
 * @description 缓存切面的实现
 * @version 1.0
 */
@Component
@Aspect
public class KingGmallCacheAspect {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 增强方法
     *
     * @param point
     * @return
     */
    @Around("@annotation(com.king.gmall.common.cache.KingGmallCache)")
    public Object getKingGmallCache(ProceedingJoinPoint point) {
        Object result = null;
        try {
            //获取方法签名
            MethodSignature methodSignature = (MethodSignature) point.getSignature();

            //获取方法参数列表
            Object[] args = point.getArgs();
            //获取方法注解
            KingGmallCache kingGmallCache = methodSignature.getMethod().getAnnotation(KingGmallCache.class);
            //key前缀:例如king:
            String prefix = kingGmallCache.prefix();
            //key
            String key = prefix + Arrays.asList(args).toString();
            result = cacheHit(methodSignature, key);
            //如果缓存有,直接返回
            if (result != null) {
                return result;
            }
            //初始化分布式锁
            RLock lock = redissonClient.getLock(key + ":lock");
            //加锁
            if (lock.tryLock(100, 100, TimeUnit.SECONDS)) {
                try {
                    //查询数据库(调用目标方法)
                    result = point.proceed();
                    if (result != null) {
                        //如果不为空,存入redis
                        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(result), 24 * 60 * 60, TimeUnit.SECONDS);
                    } else {
                        //如果数据库也没有,反射创建空对象,存入redis
                        result = methodSignature.getReturnType().newInstance();
                        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(result), 300, TimeUnit.SECONDS);
                    }

                    return result;

                } catch (Throwable e) {
                    throw new RuntimeException(e);
                } finally {
                    //释放锁
                    lock.unlock();
                }


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 从缓存获取数据
     *
     * @return
     */
    private Object cacheHit(MethodSignature methodSignature, String key) {
        //查询缓存
        String result = (String) redisTemplate.opsForValue().get(key);
        //缓存有数据
        if (result != null) {
            //反序列化返回
            return JSONObject.parseObject(result, methodSignature.getReturnType());
        }
        //没有数据返回空
        return null;
    }
}
