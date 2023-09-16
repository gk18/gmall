package com.king.gmall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.king.gmall.model.product.*;

import java.util.List;

/***
 * ClassName: ManageService
 * Package: com.king.gmall.product.service
 * @author GK
 * @date 2023/9/15 15:07
 * @description 管理分类接口
 * @version 1.0
 */
public interface ManageService {
    /**
     * 查询所有一级分类
     *
     * @return
     */
    List<BaseCategory1> findBaseCategory1();

    /**
     *  查询一级分类对应的二级分类
     * @return
     */
    List<BaseCategory2> findBaseCategory2(Long category1Id);

    /**
     * 查询二级分类对应的三级分类
     * @param category2Id
     * @return
     */
    List<BaseCategory3> findBaseCategory3(Long category2Id);

    /**
     * 保存或修改平台属性
     * @param baseAttrInfo
     */
    void saveOrAlterBaseAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     * 查询三级分类的平台属性列表
     *
     * @param categoryId
     * @return
     */
    List<BaseAttrInfo> getBaseAttrInfo(Long categoryId);

    /**
     * 根据id删除平台属性
     * @param attrId
     */
    void removeBaseAttrInfo(Long attrId);

    /**
     * 分页查询品牌
     *
     * @return
     */
    IPage<BaseTrademark> pageListBaseTrademark(Integer page, Integer size);

    /**
     * 查询所有品牌
     *
     * @return
     */
    List<BaseTrademark> listBaseTrademark();

    /**
     * 销售属性列表
     * @return
     */
    List<BaseSaleAttr> listBaseSaleAtt();

    /**
     * 新增SpuInfo
     *
     * @param spuInfo
     */
    void saveSpuInfo(SpuInfo spuInfo);

    /**
     * 根据第三级分类id分页查询SpuInfo
     *
     * @param page
     * @param size
     * @param category3Id
     * @return
     */
     IPage<SpuInfo> pageSpuInfo(Integer page, Integer size, Long category3Id);
}
