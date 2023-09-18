package com.king.gmall.item.client.fallback;

import com.king.gmall.common.result.Result;
import com.king.gmall.item.client.ItemFeignClient;
import org.springframework.stereotype.Component;

/***
 * ClassName: ItemFeignClientFallback
 * Package: com.king.gmall.item.client.fallback
 * @author GK
 * @date 2023/9/18 20:56
 * @description 处理item降级
 * @version 1.0
 */
@Component
public class ItemFeignClientFallback implements ItemFeignClient {
    @Override
    public Result getSkuDetails(Long skuId) {
        return null;
    }
}
