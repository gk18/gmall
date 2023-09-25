package com.king.gmall.list.service.impl;

import com.king.gmall.list.dao.GoodsDao;
import com.king.gmall.list.service.GoodsService;
import com.king.gmall.model.list.Goods;
import com.king.gmall.model.list.SearchAttr;
import com.king.gmall.model.product.BaseAttrInfo;
import com.king.gmall.model.product.BaseCategoryView;
import com.king.gmall.model.product.BaseTrademark;
import com.king.gmall.model.product.SkuInfo;
import com.king.gmall.product.client.ProductFeignClient;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/***
 * ClassName: GoodsServiceImpl
 * Package: com.king.gmall.list.service.impl
 * @author GK
 * @date 2023/9/23 18:57
 * @description es中商品管理的接口实现类
 * @version 1.0
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsDao goodsDao;
    @Resource
    private ProductFeignClient productFeignClient;
    /**
     * 从mysql添加商品到es
     *
     * @param skuId
     */
    @Override
    public void addSkuFromMysqlToEs(Long skuId) {
        //获取skuInfo数据
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if(skuInfo == null || skuInfo.getId() == null) {
            return;
        }
        Goods goods = new Goods();
        //商品信息
        goods.setId(skuInfo.getId());
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setPrice(productFeignClient.getSkuPrice(skuId).doubleValue());
        goods.setCreateTime(new Date());
        //品牌信息
        BaseTrademark baseTrademark = productFeignClient.getBaseTrademarkById(skuInfo.getTmId());
        goods.setTmId(baseTrademark.getId());
        goods.setTmName(baseTrademark.getTmName());
        goods.setTmLogoUrl(baseTrademark.getLogoUrl());
        //分类信息
        BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
        goods.setCategory1Id(categoryView.getCategory1Id());
        goods.setCategory1Name(categoryView.getCategory1Name());
        goods.setCategory2Id(categoryView.getCategory2Id());
        goods.setCategory2Name(categoryView.getCategory2Name());
        goods.setCategory3Id(categoryView.getCategory3Id());
        goods.setCategory3Name(categoryView.getCategory3Name());
        //平台属性集合
        List<BaseAttrInfo> baseAttrInfoList = productFeignClient.getAttrNameAndValueBySkuId(skuInfo.getId());
        List<SearchAttr> attrs = baseAttrInfoList.stream().map(baseAttrInfo -> {
            SearchAttr searchAttr = new SearchAttr();
            searchAttr.setAttrId(baseAttrInfo.getId());
            searchAttr.setAttrName(baseAttrInfo.getAttrName());
            searchAttr.setAttrValue(baseAttrInfo.getAttrValueList().get(0).getValueName());
            return searchAttr;
        }).collect(Collectors.toList());
        goods.setAttrs(attrs);
        //存入es
        goodsDao.save(goods);
    }

    /**
     * 移除es中的商品
     *
     * @param goodsId
     */
    @Override
    public void removeGoodsFromEs(Long goodsId) {
        goodsDao.deleteById(goodsId);
    }
}
