package com.king.gmall.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.king.gmall.common.execption.GmallException;
import com.king.gmall.common.result.ResultCodeEnum;
import com.king.gmall.model.product.BaseAttrInfo;
import com.king.gmall.product.mapper.BaseAttrInfoMapper;
import com.king.gmall.product.service.BaseAttrInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/***
 * ClassName: BaseAttrInfoServiceImpl
 * Package: com.king.gmall.product.service.impl
 * @author GK
 * @date 2023/9/14 19:22
 * @description
 * @version 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAttrInfoServiceImpl implements BaseAttrInfoService {
    @Resource
    private BaseAttrInfoMapper baseAttrInfoMapper;

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<BaseAttrInfo> findAll() {
        return baseAttrInfoMapper.selectList(null);
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @Override
    public BaseAttrInfo getById(Long id) {

        return baseAttrInfoMapper.selectById(id);
    }

    /**
     * 新增
     *
     * @param baseAttrInfo
     */
    @Override
    public void create(BaseAttrInfo baseAttrInfo) {
        //判断新增数据是否为空,id和name不能为空,主键自增,判断name即可
        if (baseAttrInfo == null || StringUtils.isEmpty(baseAttrInfo.getAttrName())) {
            throw new GmallException("参数不能为空", ResultCodeEnum.FAIL.getCode());
        }
        int insert = baseAttrInfoMapper.insert(baseAttrInfo);
        if (insert <= 0) {
            throw new GmallException("新增平台属性失败", ResultCodeEnum.FAIL.getCode());
        }

    }

    /**
     * 修改
     *
     * @param baseAttrInfo
     */
    @Override
    public void update(BaseAttrInfo baseAttrInfo) {
        //判断修改数据是否为空,id和name不能为空,主键自增,判断name即可
        if (baseAttrInfo == null || StringUtils.isEmpty(baseAttrInfo.getAttrName())) {
            throw new GmallException("参数不能为空", ResultCodeEnum.FAIL.getCode());
        }
        int update = baseAttrInfoMapper.updateById(baseAttrInfo);
        if (update < 0) {
            throw new GmallException("修改平台属性失败", ResultCodeEnum.FAIL.getCode());
        }
    }

    /**
     * 根据主键删除
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        int delete = baseAttrInfoMapper.deleteById(id);

        if (delete <= 0) {
            throw new GmallException("删除平台属性失败", ResultCodeEnum.FAIL.getCode());
        }

    }

    /**
     * 分页查询
     *
     * @param current
     * @param size
     * @return
     */
    @Override
    public IPage<BaseAttrInfo> page(Integer current, Integer size) {
        return baseAttrInfoMapper.selectPage(new Page<>(current, size), null);
    }

    /**
     * 条件查询
     *
     * @param baseAttrInfo
     * @return
     */
    @Override
    public List<BaseAttrInfo> search(BaseAttrInfo baseAttrInfo) {
        //条件为空,查询所有
        if (baseAttrInfo == null) {
            return baseAttrInfoMapper.selectList(null);
        }
        //构建查询条件
        LambdaQueryWrapper<BaseAttrInfo> queryWrapper = buildQueryParams(baseAttrInfo);
        //查询并返回
        return baseAttrInfoMapper.selectList(queryWrapper);
    }


    /**
     * 分页条件查询
     *
     * @param baseAttrInfo
     * @param current
     * @param size
     * @return
     */
    @Override
    public IPage<BaseAttrInfo> page(BaseAttrInfo baseAttrInfo, Integer current, Integer size) {
        //条件为空,直接查询分页数据返回
        if (baseAttrInfo == null) {
            return this.page(current, size);
        }
        //构建查询条件
        LambdaQueryWrapper<BaseAttrInfo> queryWrapper = buildQueryParams(baseAttrInfo);
        return baseAttrInfoMapper.selectPage(new Page<>(current, size), queryWrapper);

    }
    /**
     * 拼接查询条件
     * @param baseAttrInfo
     * @return
     */
    private LambdaQueryWrapper<BaseAttrInfo> buildQueryParams(BaseAttrInfo baseAttrInfo) {
        LambdaQueryWrapper<BaseAttrInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (baseAttrInfo.getId() != null) {

            //id做条件
            queryWrapper.eq(BaseAttrInfo::getId, baseAttrInfo.getId());
        }
        if (!StringUtils.isEmpty(baseAttrInfo.getAttrName())) {

            //name做条件
            queryWrapper.like(BaseAttrInfo::getAttrName, baseAttrInfo.getAttrName());
        }
        if (baseAttrInfo.getCategoryId() != null) {

            //分类id做条件
            queryWrapper.eq(BaseAttrInfo::getCategoryId, baseAttrInfo.getCategoryId());
        }
        return queryWrapper;
    }
}
