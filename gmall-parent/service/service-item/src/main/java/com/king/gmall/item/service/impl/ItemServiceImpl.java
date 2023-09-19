package com.king.gmall.item.service.impl;


import com.king.gmall.item.service.ItemService;

import com.king.gmall.model.product.BaseCategoryView;
import com.king.gmall.model.product.SkuImage;
import com.king.gmall.model.product.SkuInfo;
import com.king.gmall.model.product.SpuSaleAttr;
import com.king.gmall.product.client.ProductFeignClient;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/***
 * ClassName: ItemServiceImpl
 * Package: com.king.gmall.item.service.impl
 * @author GK
 * @date 2023/9/18 18:44
 * @description 商品详情服务实现类
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 获取sku详细信息,用于生成sku详情页面
     *
     * @param skuId
     * @return
     */
    @Override
    public Map<String, Object> getSkuDetails(Long skuId) {
        //组装前端需要展示的数据
        Map map = new ConcurrentHashMap<>();
        //使用异步编排优化
        //1.查询skuInfo
        CompletableFuture<SkuInfo> future1 = CompletableFuture.supplyAsync(() -> {

            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
            if (skuInfo == null || skuInfo.getSpuId() == null) {
                return null;
            }
            map.put("skuInfo", skuInfo);
            return skuInfo;
        },threadPoolExecutor);

        //2.分类查询
        CompletableFuture<Void> future2 = future1.thenAcceptAsync(skuInfo -> {
            if (skuInfo == null || skuInfo.getSpuId() == null) {
                return;
            }
            BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
            map.put("categoryView", categoryView);
        },threadPoolExecutor);
        //3.图片数据
        CompletableFuture<Void> future3 = future1.thenAcceptAsync(skuInfo -> {
            if (skuInfo == null || skuInfo.getSpuId() == null) {
                return;
            }
            List<SkuImage> imageList = productFeignClient.getImageList(skuId);
            map.put("imageList", imageList);
        },threadPoolExecutor);

        //4.最新价格
        CompletableFuture<Void> future4 = future1.thenAcceptAsync(skuInfo -> {
            if (skuInfo == null || skuInfo.getSpuId() == null) {
                return;
            }
            BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
            map.put("skuPrice", skuPrice);
        },threadPoolExecutor);

        //5.销售属性
        CompletableFuture<Void> future5 = future1.thenAcceptAsync(skuInfo -> {
            if (skuInfo == null || skuInfo.getSpuId() == null) {
                return;
            }
            List<SpuSaleAttr> spuSaleAttrListBySkuIdAndSpuId =
                    productFeignClient.getSpuAttrListCheckBySku(skuId, skuInfo.getSpuId());
            map.put("spuSaleAttrListCheckBySku", spuSaleAttrListBySkuIdAndSpuId);
        },threadPoolExecutor);

        //6.页面跳转键值对
        CompletableFuture<Void> future6 = future1.thenAcceptAsync(skuInfo -> {
            if (skuInfo == null || skuInfo.getSpuId() == null) {
                return;
            }
            Map skuValueIdsMap = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
            map.put("skuValueIdsMap", skuValueIdsMap);
        },threadPoolExecutor);
        CompletableFuture.allOf(future2, future3, future4, future5, future6).join();
        return map;


    }


}
