package com.king.gmall.item.service;

import java.util.Map;

/***
 * ClassName: ItemService
 * Package: com.king.gmall.item.service
 * @author GK
 * @date 2023/9/18 18:42
 * @description 商品详情接口
 * @version 1.0
 */
public interface ItemService {
    /**
     * 获取sku详细信息,用于生成sku详情页面
     * @param skuId
     * @return
     */
    Map<String,Object> getSkuDetails(Long skuId);
}
