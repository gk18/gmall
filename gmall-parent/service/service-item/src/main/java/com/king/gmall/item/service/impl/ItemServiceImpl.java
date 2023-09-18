package com.king.gmall.item.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.king.gmall.common.result.Result;
import com.king.gmall.item.service.ItemService;

import com.king.gmall.model.product.BaseCategoryView;
import com.king.gmall.model.product.SkuInfo;
import com.king.gmall.model.product.SpuSaleAttr;
import com.king.gmall.product.client.ProductFeignClient;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    /**
     * 获取sku详细信息,用于生成sku详情页面
     *
     * @param skuId
     * @return
     */
    @Override
    public Map<String, Object> getSkuDetails(Long skuId) {
        //远程调用商品微服务service-product,获取sku的info  image 销售属性 平台属性信息
        //远程调用获取skuInfo
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        //获取类别
        BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
        //获取价格
        BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
        //获取销售属性与sku键值对
        Map skuValueIdsMap = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
        //获取指定spu与sku的商品的销售属性值信息
        List<SpuSaleAttr> spuSaleAttrListCheckBySku =
                productFeignClient.getSpuAttrListCheckBySku(skuId, skuInfo.getSpuId());
        //包装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("skuInfo", skuInfo);
        result.put("categoryView", categoryView);
        result.put("skuPrice", skuPrice);
        //map类型最好进行json串转换,方便前端进行处理
        result.put("skuValueIdsMap", JSONObject.toJSONString(skuValueIdsMap));
        result.put("spuSaleAttrListCheckBySku", spuSaleAttrListCheckBySku);
        //返回结果
        return result;

    }

}
