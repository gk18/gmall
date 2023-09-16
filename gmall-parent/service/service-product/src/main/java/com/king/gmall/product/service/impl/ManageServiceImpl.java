package com.king.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.king.gmall.common.execption.GmallException;
import com.king.gmall.common.result.ResultCodeEnum;
import com.king.gmall.model.base.BaseEntity;
import com.king.gmall.model.product.*;
import com.king.gmall.product.mapper.*;
import com.king.gmall.product.service.ManageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/***
 * ClassName: ManageServiceImpl
 * Package: com.king.gmall.product.service.impl
 * @author GK
 * @date 2023/9/15 15:12
 * @description 分类管理业务层
 * @version 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManageServiceImpl implements ManageService {
    @Resource
    private BaseCategory1Mapper baseCategory1Mapper;
    @Resource
    private BaseCategory2Mapper baseCategory2Mapper;
    @Resource
    private BaseCategory3Mapper baseCategory3Mapper;
    @Resource
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Resource
    private BaseAttrValueMapper baseAttrValueMapper;
    /**
     * 查询所有一级分类
     *
     * @return
     */
    @Override
    public List<BaseCategory1> findBaseCategory1() {
        return baseCategory1Mapper.selectList(null);
    }

    /**
     * 查询一级分类对应的二级分类
     *
     * @param category1Id
     * @return
     */
    @Override
    public List<BaseCategory2> findBaseCategory2(Long category1Id) {
        return baseCategory2Mapper.selectList(
                new LambdaQueryWrapper<BaseCategory2>()
                        .eq(BaseCategory2::getCategory1Id,category1Id));
    }

    /**
     * 查询二级分类对应的三级分类
     *
     * @param category2Id
     * @return
     */
    @Override
    public List<BaseCategory3> findBaseCategory3(Long category2Id) {
        return baseCategory3Mapper.selectList(
                new LambdaQueryWrapper<BaseCategory3>()
                        .eq(BaseCategory3::getCategory2Id,category2Id));
    }

    /**
     * 保存或修改平台属性
     *
     * @param baseAttrInfo
     */
    @Override
    public void saveOrAlterBaseAttrInfo(BaseAttrInfo baseAttrInfo) {
        //如果平台属性为空,抛出异常
        if(baseAttrInfo == null || StringUtils.isEmpty(baseAttrInfo.getAttrName())) {
            throw new GmallException("参数不能为空", ResultCodeEnum.FAIL.getCode());
        }
        //判断id是否存在,存在则修改,不存在则新增
        if(baseAttrInfo.getId() == null) {
            //直接保存
            int insert = baseAttrInfoMapper.insert(baseAttrInfo);
            if(insert <= 0) {
                throw new GmallException("保存平台属性失败",ResultCodeEnum.FAIL.getCode());
            }

        }else {
            //进行修改
            int update = baseAttrInfoMapper.updateById(baseAttrInfo);
            if(update < 0) {
                throw new GmallException("更新平台属性失败",ResultCodeEnum.FAIL.getCode());
            }
            //删除平台属性值(先删再增等于修改)
            int delete = baseAttrValueMapper.delete(
                    new LambdaQueryWrapper<BaseAttrValue>()
                            .eq(BaseAttrValue::getAttrId, baseAttrInfo.getId()));
            if(delete < 0) {
                throw new GmallException("更新平台属性值失败",ResultCodeEnum.FAIL.getCode());
            }
        }
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if(attrValueList == null || attrValueList.size() == 0) {
            return;
        }
        //保存或修改成功,mybatis把主键返回给baseAttrInfo
        attrValueList.stream().forEach(baseAttrValue -> {
            //设置attrId
            baseAttrValue.setAttrId(baseAttrInfo.getId());

        });
        int batchInsert = baseAttrValueMapper.batchInsert(attrValueList);
        if(batchInsert <= 0) {
            throw new GmallException("保存平台属性值失败",ResultCodeEnum.FAIL.getCode());
        }



    }

    /**
     * 查询三级分类的平台属性列表
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<BaseAttrInfo> getBaseAttrInfo(Long categoryId) {
        return baseAttrInfoMapper.selectAttrInfo(categoryId);

    }

    /**
     * 根据id删除平台属性
     *
     * @param attrId
     */
    @Override
    public void removeBaseAttrInfo(Long attrId) {
        //删除平台属性
        int delete = baseAttrInfoMapper.deleteById(attrId);
        if(delete <= 0) {
            throw new GmallException("删除平台属性失败",ResultCodeEnum.FAIL.getCode());
        }
        //删除平台属性值
        int deleteValue = baseAttrValueMapper.delete(
                new LambdaQueryWrapper<BaseAttrValue>()
                        .eq(BaseAttrValue::getAttrId, attrId));
        if(deleteValue < 0) {
            throw new GmallException("删除平台属性值失败",ResultCodeEnum.FAIL.getCode());
        }
    }


}
