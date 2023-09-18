package com.king.gmall.product.controller;

import com.king.gmall.model.product.BaseCategoryView;
import com.king.gmall.model.product.SkuInfo;
import com.king.gmall.model.product.SpuSaleAttr;
import com.king.gmall.product.service.ProductClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/***
 * ClassName: InnerProductController
 * Package: com.king.gmall.product.controller
 * @author GK
 * @date 2023/9/18 15:25
 * @description 供内部服务调用
 * @version 1.0
 */
@RestController
@RequestMapping("/admin/product")
public class InnerProductController {
    @Resource
    private ProductClientService productClientService;

    /**
     * 查询sku信息
     *
     * @param skuId
     * @return
     */
    @GetMapping("/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId) {
        return productClientService.getSkuInfo(skuId);
    }

    /**
     * 根据三级分类id查询分类信息
     *
     * @param category3Id
     * @return
     */
    @GetMapping("/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable("category3Id") Long category3Id) {
        return productClientService.getCategoryViewByCategory3Id(category3Id);
    }

    /**
     * 查询sku价格
     *
     * @param skuId
     * @return
     */
    @GetMapping("/getSkuPrice/{skuId}")
    public BigDecimal getSkuPrice(@PathVariable("skuId") Long skuId) {
        return productClientService.getSkuPrice(skuId);
    }

    /**
     * 查询指定spu下属性值与skuId对应关系
     *
     * @param skuId
     * @param spuId
     * @return
     */
    @GetMapping("/getSpuAttrListCheckBySku/{skuId}/{spuId}")
    public List<SpuSaleAttr> getSpuAttrListCheckBySku(@PathVariable("skuId") Long skuId,
                                                      @PathVariable("spuId") Long spuId) {
        return productClientService.getSpuSaleAttrListCheckBySku(skuId, spuId);
    }

    /**
     * 根据spuId查询集合属性
     * @param spuId
     * @return
     */
    @GetMapping("/getSkuValueIdsMap/{spuId}")
    public Map getSkuValueIdsMap(@PathVariable("spuId") Long spuId){
        return productClientService.getSkuValueIdsMap(spuId);
    }
}
