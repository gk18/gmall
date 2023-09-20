package com.king.gmall.common.cache;


import java.lang.annotation.*;

/***
 * ClassName: KingGmallCache
 * Package: com.king.gmall.common.cache
 * @author GK
 * @date 2023/9/20 19:48
 * @description 缓存注解, 实现aop
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KingGmallCache {
    String prefix() default "cache";
}
