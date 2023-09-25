package com.king.gmall.list.client;

import com.king.gmall.list.client.fallback.ListFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/***
 * ClassName: ListFeignClient
 * Package: com.king.gmall.list.client
 * @author GK
 * @date 2023/9/25 15:08
 * @description 商品搜索微服务远程调用接口
 * @version 1.0
 */
@FeignClient(
        value = "service-list",
        path = "/api/search",
        fallback = ListFeignClientFallback.class
)
public interface ListFeignClient {
    @GetMapping
    public Map<String, Object> search(@RequestParam Map<String, String> searchData);
}
