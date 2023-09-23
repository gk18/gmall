package com.king.gmall.product.client;

import com.king.gmall.common.result.Result;
import com.king.gmall.model.product.BaseCategoryView;
import com.king.gmall.model.product.SkuImage;
import com.king.gmall.model.product.SkuInfo;
import com.king.gmall.model.product.SpuSaleAttr;
import com.king.gmall.product.client.fallback.ProductFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/***
 * ClassName: ProductFeignClient
 * Package: product.client
 * @author GK
 * @date 2023/9/18 19:04
 * @description 商品管理微服务远程feign接口
 * @version 1.0
 */
@FeignClient(
        value = "service-product",
        path = "/admin/product",
        fallback = ProductFeignClientFallback.class,
        contextId = "productFeignClient")
public interface ProductFeignClient {
    /**
     * 查询sku信息
     *
     * @param skuId
     * @return
     */
    @GetMapping("/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId);
    /**
     * 查询sku图片列表
     * @param skuId
     * @return
     */
    @GetMapping("/getSkuImage/{skuId}")
    public List<SkuImage> getImageList(@PathVariable("skuId") Long skuId);

    /**
     * 根据三级分类id查询分类信息
     *
     * @param category3Id
     * @return
     */
    @GetMapping("/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable("category3Id") Long category3Id);

    /**
     * 查询sku价格
     *
     * @param skuId
     * @return
     */
    @GetMapping("/getSkuPrice/{skuId}")
    public BigDecimal getSkuPrice(@PathVariable("skuId") Long skuId);

    /**
     * 查询指定spu下属性值与skuId对应关系
     *
     * @param skuId
     * @param spuId
     * @return
     */
    @GetMapping("/getSpuAttrListCheckBySku/{skuId}/{spuId}")
    public List<SpuSaleAttr> getSpuAttrListCheckBySku(@PathVariable("skuId") Long skuId,
                                                      @PathVariable("spuId") Long spuId);

    /**
     * 根据spuId查询集合属性
     * @param spuId
     * @return
     */
    @GetMapping("/getSkuValueIdsMap/{spuId}")
    public Map getSkuValueIdsMap(@PathVariable("spuId") Long spuId);
}
