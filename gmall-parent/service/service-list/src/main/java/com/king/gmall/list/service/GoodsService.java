package com.king.gmall.list.service;

/***
 * ClassName: GoodsService
 * Package: com.king.gmall.list.service
 * @author GK
 * @date 2023/9/23 18:54
 * @description es中商品管理的接口类
 * @version 1.0
 */
public interface GoodsService {
    /**
     * 从mysql添加商品到es
     * @param skuId
     */
    void addSkuFromMysqlToEs(Long skuId);

    /**
     * 移除es中的商品
     * @param goodsId
     */
    void removeGoodsFromEs(Long goodsId);
}
