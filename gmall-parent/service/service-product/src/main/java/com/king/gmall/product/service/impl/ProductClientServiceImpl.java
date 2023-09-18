package com.king.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.king.gmall.model.product.*;
import com.king.gmall.product.mapper.*;
import com.king.gmall.product.service.ProductClientService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * ClassName: ProductClientServiceImpl
 * Package: com.king.gmall.product.service.impl
 * @author GK
 * @date 2023/9/18 15:13
 * @description 商品微服务供远程调用的业务层接口实现
 * @version 1.0
 */
@Service
public class ProductClientServiceImpl implements ProductClientService {
    @Resource
    private SkuInfoMapper skuInfoMapper;
    @Resource
    private SkuImageMapper skuImageMapper;
    @Resource
    private BaseCategoryViewMapper baseCategoryViewMapper;
    @Resource
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Resource
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    /**
     * 根据skuId获取sku信息并封装image
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        //查询sku信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (skuInfo == null) {
            return null;
        }
        //查询sku图片信息
        List<SkuImage> skuImageList = skuImageMapper.selectList(
                new LambdaQueryWrapper<SkuImage>()
                        .eq(SkuImage::getSkuId, skuId));
        //设置sku图片列表
        skuInfo.setSkuImageList(skuImageList);
        return skuInfo;
    }

    /**
     * 根据第三级分类id查询所有分类信息(三级分类id,name)
     *
     * @param category3Id
     * @return
     */
    @Override
    public BaseCategoryView getCategoryViewByCategory3Id(Long category3Id) {
        return baseCategoryViewMapper.selectById(category3Id);
    }

    /**
     * 获取sku价格
     *
     * @param skuId
     * @return
     */
    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        //查询sku信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (skuInfo == null) {
            //sku不存在直接返回0
            return BigDecimal.valueOf(0);
        }
        return skuInfo.getPrice();
    }

    /**
     * 查看指定sku的销售属性
     *
     * @param spuId
     * @param skuId
     * @return
     */
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return spuSaleAttrMapper.spuSaleAttr(skuId, spuId);
    }

    /**
     * 查询指定spu下属性值与skuId对应关系
     *
     * @param spuId
     * @return
     */
    @Override
    public Map getSkuValueIdsMap(Long spuId) {
        //初始化Map
        HashMap<Object, Object> result = new HashMap<>();
        //查询所有键值对
        List<Map> maps = skuSaleAttrValueMapper.selectSkuMap(spuId);
        maps.stream().forEach(map -> {
            result.put(map.get("sku_id"), map.get("value_id"));
        });
        return result;
    }
}
