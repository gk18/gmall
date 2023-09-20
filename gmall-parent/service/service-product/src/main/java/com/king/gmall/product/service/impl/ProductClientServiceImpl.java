package com.king.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.king.gmall.model.product.*;
import com.king.gmall.product.mapper.*;
import com.king.gmall.product.service.ProductClientService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/***
 * ClassName: ProductClientServiceImpl
 * Package: com.king.gmall.product.service.impl
 * @author GK
 * @date 2023/9/18 15:13
 * @description 商品微服务供远程调用的业务层接口实现
 * @version 1.0
 */
@Service
@Slf4j
public class ProductClientServiceImpl implements ProductClientService {
    @Resource
    private SkuInfoMapper skuInfoMapper;
    @Resource
    private SkuImageMapper skuImageMapper;
    @Resource
    private BaseCategoryViewMapper baseCategoryViewMapper;
    @Resource
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Resource
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private ServletWebServerApplicationContext webServerAppContext;

    /**
     * 根据skuId获取sku信息并封装image
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        //查询sku信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (skuInfo == null) {
            return null;
        }
        //查询sku图片信息
        List<SkuImage> skuImageList = skuImageMapper.selectList(
                new LambdaQueryWrapper<SkuImage>()
                        .eq(SkuImage::getSkuId, skuId));
        //设置sku图片列表
        skuInfo.setSkuImageList(skuImageList);
        return skuInfo;
    }

    /**
     * 使用redis获取sku信息
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuInfo getSkuInfoByRedis(Long skuId) {

        RLock lock = redissonClient.getLock("lock");
        try {
            if (lock.tryLock(100, 100, TimeUnit.SECONDS)) {
                Thread.currentThread().getId();
                //查询缓存次数
                Integer num = (Integer) redisTemplate.opsForValue().get("num");
                if (num != null) {
                    num++;
                    redisTemplate.opsForValue().set("num", num);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        log.info(webServerAppContext.getWebServer().getPort() + "");
        //先查询redis缓存
        SkuInfo skuInfo = (SkuInfo) redisTemplate.opsForValue().get("sku:info:" + skuId);
        if (skuInfo != null) {
            return skuInfo;
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //缓存未命中则加锁
        if (redisTemplate.opsForValue().setIfAbsent("sku:lock" + skuId, uuid, 100, TimeUnit.SECONDS)) {
            try {
                //再查一次缓存
                skuInfo = (SkuInfo) redisTemplate.opsForValue().get("sku:info:" + skuId);
                if (skuInfo != null) {
                    return skuInfo;
                }
                //查询数据库
                skuInfo = skuInfoMapper.selectById(skuId);
                Integer num = (Integer) redisTemplate.opsForValue().get("king");
                if (num != null) {
                    num++;
                    //查询数据库次数
                    redisTemplate.opsForValue().set("king", num);
                }
                //如果数据库不存在
                if (skuInfo == null) {
                    skuInfo = new SkuInfo();
                    //防止穿透写入缓存
                    redisTemplate.opsForValue().set("sku:info:" + skuId, skuInfo, 300, TimeUnit.SECONDS);

                }
                redisTemplate.opsForValue().set("sku:info:" + skuId, skuInfo, 1, TimeUnit.DAYS);

            } finally {
                DefaultRedisScript<Integer> script = new DefaultRedisScript<>();
                script.setScriptText("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end ");
                //释放锁
                redisTemplate.execute(script, Arrays.asList("sku:lock" + skuId), uuid);
            }

        } else {
            //获取锁失败,睡一会再执行
            try {
                TimeUnit.MILLISECONDS.sleep(200);
                return getSkuInfoByRedis(skuId);
            } catch (InterruptedException e) {

                e.printStackTrace();
                return null;
            }


        }

        return skuInfo;
    }

    /**
     * 使用Redisson加锁获取sku信息
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuInfo getSkuInfoByRedisson(Long skuId) {
        //查询redis缓存
        SkuInfo skuInfo = (SkuInfo) redisTemplate.opsForValue().get("sku:info" + skuId);
        if (skuInfo != null) {
            return skuInfo;
        }
        //未命中,加锁
        try {
            if (redissonClient.getLock("lock").tryLock(100, 100, TimeUnit.SECONDS)) {
                try {
                    //查询数据库
                    skuInfo = skuInfoMapper.selectById(skuId);
                    //判断是否存在
                    if (skuInfo == null) {
                        skuInfo = new SkuInfo();
                        //为空也创建一个放入缓存,防止穿透
                        redisTemplate.opsForValue().set("sku:info" + skuId, skuInfo, 300, TimeUnit.SECONDS);

                    } else {
                        //存在放入缓存
                        redisTemplate.opsForValue().set("sku:info" + skuId, skuInfo, 24 * 60 * 60, TimeUnit.SECONDS);
                    }
                    return skuInfo;
                    //解锁
                } finally {
                    redissonClient.getLock("lock").unlock();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询sku的数据出现异常,异常的原因为:" + e.getMessage());
        }
        return null;
    }

    /**
     * 根据skuId查询图片列表
     *
     * @param skuId
     * @return
     */
    @Override
    public List<SkuImage> getSkuImageBySkuId(Long skuId) {
        return skuImageMapper.selectList(new LambdaQueryWrapper<SkuImage>()
                .eq(SkuImage::getSkuId, skuId));
    }

    /**
     * 根据第三级分类id查询所有分类信息(三级分类id,name)
     *
     * @param category3Id
     * @return
     */
    @Override
    public BaseCategoryView getCategoryViewByCategory3Id(Long category3Id) {
        return baseCategoryViewMapper.selectById(category3Id);
    }

    /**
     * 获取sku价格
     *
     * @param skuId
     * @return
     */
    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        //查询sku信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (skuInfo == null) {
            //sku不存在直接返回0
            return BigDecimal.valueOf(0);
        }
        return skuInfo.getPrice();
    }

    /**
     * 查看指定sku的销售属性
     *
     * @param spuId
     * @param skuId
     * @return
     */
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return spuSaleAttrMapper.spuSaleAttr(skuId, spuId);
    }

    /**
     * 查询指定spu下属性值与skuId对应关系
     *
     * @param spuId
     * @return
     */
    @Override
    public Map getSkuValueIdsMap(Long spuId) {
        //初始化Map
        HashMap<Object, Object> result = new HashMap<>();
        //查询所有键值对
        List<Map> maps = skuSaleAttrValueMapper.selectSkuSaleValueId(spuId);
        maps.stream().forEach(map -> {
            //销售属性值列表做键,skuId做值
            result.put(map.get("value_id"), map.get("sku_id"));
        });
        return result;
    }
}
