package com.king.gmall.item.client;

import com.king.gmall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/***
 * ClassName: ItemFeignClient
 * Package: com.king.gmall.item.client
 * @author GK
 * @date 2023/9/18 20:54
 * @description
 * @version 1.0
 */
@FeignClient(name = "service-item",path = "/gmall/item")
public interface ItemFeignClient {
    @GetMapping("/getSkuDetails/{skuId}")
    public Result getSkuDetails(@PathVariable("skuId") Long skuId);
}
