package com.king.gmall.product.service;

import com.king.gmall.model.product.BaseCategoryView;
import com.king.gmall.model.product.SkuImage;
import com.king.gmall.model.product.SkuInfo;
import com.king.gmall.model.product.SpuSaleAttr;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/***
 * ClassName: ProductClientService
 * Package: com.king.gmall.product.service
 * @author GK
 * @date 2023/9/18 15:11
 * @description 商品微服务供远程调用的业务层接口
 * @version 1.0
 */
public interface ProductClientService {
    /**
     * 根据skuId获取sku信息并封装image
     * @param skuId
     * @return
     */
    SkuInfo getSkuInfo(Long skuId);

    /**
     * 使用redis加锁获取sku信息
     * @param skuId
     * @return
     */
    SkuInfo getSkuInfoByRedis(Long skuId);

    /**
     * 使用Redisson加锁获取sku信息
     * @param skuId
     * @return
     */
    SkuInfo getSkuInfoByRedisson(Long skuId);

    /**
     * 根据skuId查询图片列表
     * @param skuId
     * @return
     */
    List<SkuImage> getSkuImageBySkuId(Long skuId);

    /**
     * 根据第三级分类id查询所有分类信息(三级分类id,name)
     *
     * @param category3Id
     * @return
     */
    BaseCategoryView getCategoryViewByCategory3Id(Long category3Id);

    /**
     * 获取sku价格
     *
     * @param skuId
     * @return
     */
    BigDecimal getSkuPrice(Long skuId);

    /**
     * 查看指定sku的销售属性
     *
     * @param skuId
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId);

    /**
     * 查询指定spu下属性值与skuId对应关系
     * @param spuId
     * @return
     */
    Map getSkuValueIdsMap(Long spuId);
}
