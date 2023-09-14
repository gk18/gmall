package com.king.gmall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.king.gmall.model.product.BaseAttrInfo;

import java.util.List;

/***
 * ClassName: BaseAttrInfoService
 * Package: com.king.gmall.product.service
 * @author GK
 * @date 2023/9/14 19:20
 * @description 平台属性管理的接口类
 * @version 1.0
 */
public interface BaseAttrInfoService {
    /**
     * 查询所有
     * @return
     */
    List<BaseAttrInfo> findAll();

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    BaseAttrInfo getById(Long id);

    /**
     * 新增
     * @param baseAttrInfo
     */
    void create(BaseAttrInfo baseAttrInfo);

    /**
     * 修改
     *
     * @param baseAttrInfo
     */
    void update(BaseAttrInfo baseAttrInfo);

    /**
     * 根据主键删除
     * @param id
     */
    void deleteById(Long id);

    /**
     * 分页查询
     *
     * @param current
     * @param size
     * @return
     */
    IPage<BaseAttrInfo> page(Integer current, Integer size);

    /**
     * 条件查询
     * @param baseAttrInfo
     * @return
     */
    List<BaseAttrInfo> search(BaseAttrInfo baseAttrInfo);

    /**
     * 分页条件查询
     *
     * @param baseAttrInfo
     * @param current
     * @param size
     * @return
     */
    IPage<BaseAttrInfo> page(BaseAttrInfo baseAttrInfo, Integer current, Integer size);

}
