package com.king.gmall.product.client.fallback;

import com.king.gmall.common.result.Result;
import com.king.gmall.model.product.*;
import org.springframework.stereotype.Component;
import com.king.gmall.product.client.ProductFeignClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/***
 * ClassName: ProductFeignClientFallback
 * Package: product.client.fallback
 * @author GK
 * @date 2023/9/18 19:09
 * @description 商品管理微服务调用降级类
 * @version 1.0
 */
@Component
public class ProductFeignClientFallback implements ProductFeignClient {

    /**
     * 查询sku信息
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        return null;
    }

    /**
     * 查询sku图片列表
     *
     * @param skuId
     * @return
     */
    @Override
    public List<SkuImage> getImageList(Long skuId) {
        return null;
    }

    /**
     * 根据三级分类id查询分类信息
     *
     * @param category3Id
     * @return
     */
    @Override
    public BaseCategoryView getCategoryView(Long category3Id) {
        return null;
    }

    /**
     * 查询sku价格
     *
     * @param skuId
     * @return
     */
    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        return null;
    }

    /**
     * 查询指定spu下属性值与skuId对应关系
     *
     * @param skuId
     * @param spuId
     * @return
     */
    @Override
    public List<SpuSaleAttr> getSpuAttrListCheckBySku(Long skuId, Long spuId) {
        return null;
    }

    /**
     * 根据spuId查询集合属性
     *
     * @param spuId
     * @return
     */
    @Override
    public Map getSkuValueIdsMap(Long spuId) {
        return null;
    }

    /**
     * 根据品牌id查询品牌数据
     *
     * @param id
     * @return
     */
    @Override
    public BaseTrademark getBaseTrademarkById(Long id) {
        return null;
    }

    /**
     * 根据skuId查询平台属性和平台属性值
     *
     * @param skuId
     * @return
     */
    @Override
    public List<BaseAttrInfo> getAttrNameAndValueBySkuId(Long skuId) {
        return null;
    }
}
