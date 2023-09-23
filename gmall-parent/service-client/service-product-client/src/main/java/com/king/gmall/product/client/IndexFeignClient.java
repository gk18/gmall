package com.king.gmall.product.client;

import com.alibaba.fastjson.JSONObject;
import com.king.gmall.product.client.fallback.IndexFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/***
 * ClassName: IndexFeignClient
 * Package: com.king.gmall.product.client
 * @author GK
 * @date 2023/9/22 20:52
 * @description 远程调用service-product微服务接口
 * @version 1.0
 */
@FeignClient(
        value = "service-product",
        path = "/index/product",
        fallback = IndexFeignClientFallback.class,
        contextId = "indexFeignClient")
public interface IndexFeignClient {
    /**
     * 首页分类数据构建
     * @return
     */
    @GetMapping("/getIndexCategory")
    public List<JSONObject> getIndexCategory();
}
