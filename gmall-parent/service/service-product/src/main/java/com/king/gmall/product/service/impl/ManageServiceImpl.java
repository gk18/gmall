package com.king.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.king.gmall.common.execption.GmallException;
import com.king.gmall.common.result.ResultCodeEnum;
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
    @Resource
    private BaseTrademarkMapper baseTrademarkMapper;
    @Resource
    private BaseSaleAttrMapper baseSaleAttrMapper;
    @Resource
    private SpuInfoMapper spuInfoMapper;
    @Resource
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Resource
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Resource
    private SpuImageMapper spuImageMapper;

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
                        .eq(BaseCategory2::getCategory1Id, category1Id));
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
                        .eq(BaseCategory3::getCategory2Id, category2Id));
    }

    /**
     * 保存或修改平台属性
     *
     * @param baseAttrInfo
     */
    @Override
    public void saveOrAlterBaseAttrInfo(BaseAttrInfo baseAttrInfo) {
        //如果平台属性为空,抛出异常
        if (baseAttrInfo == null || StringUtils.isEmpty(baseAttrInfo.getAttrName())) {
            throw new GmallException("参数不能为空", ResultCodeEnum.FAIL.getCode());
        }
        //判断id是否存在,存在则修改,不存在则新增
        if (baseAttrInfo.getId() == null) {
            //直接保存
            int insert = baseAttrInfoMapper.insert(baseAttrInfo);
            if (insert <= 0) {
                throw new GmallException("保存平台属性失败", ResultCodeEnum.FAIL.getCode());
            }

        } else {
            //进行修改
            int update = baseAttrInfoMapper.updateById(baseAttrInfo);
            if (update < 0) {
                throw new GmallException("更新平台属性失败", ResultCodeEnum.FAIL.getCode());
            }
            //删除平台属性值(先删再增等于修改)
            int delete = baseAttrValueMapper.delete(
                    new LambdaQueryWrapper<BaseAttrValue>()
                            .eq(BaseAttrValue::getAttrId, baseAttrInfo.getId()));
            if (delete < 0) {
                throw new GmallException("更新平台属性值失败", ResultCodeEnum.FAIL.getCode());
            }
        }
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if (attrValueList == null || attrValueList.size() == 0) {
            return;
        }
        //保存或修改成功,mybatis把主键返回给baseAttrInfo
        attrValueList.stream().forEach(baseAttrValue -> {
            //设置attrId
            baseAttrValue.setAttrId(baseAttrInfo.getId());

        });
        int batchInsert = baseAttrValueMapper.batchInsert(attrValueList);
        if (batchInsert <= 0) {
            throw new GmallException("保存平台属性值失败", ResultCodeEnum.FAIL.getCode());
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
        if (delete <= 0) {
            throw new GmallException("删除平台属性失败", ResultCodeEnum.FAIL.getCode());
        }
        //删除平台属性值
        int deleteValue = baseAttrValueMapper.delete(
                new LambdaQueryWrapper<BaseAttrValue>()
                        .eq(BaseAttrValue::getAttrId, attrId));
        if (deleteValue < 0) {
            throw new GmallException("删除平台属性值失败", ResultCodeEnum.FAIL.getCode());
        }
    }

    /**
     * 分页查询品牌
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public IPage<BaseTrademark> pageListBaseTrademark(Integer page, Integer size) {
        return baseTrademarkMapper.selectPage(new Page<BaseTrademark>(page, size), null);
    }

    /**
     * 查询所有品牌
     *
     * @return
     */
    @Override
    public List<BaseTrademark> listBaseTrademark() {
        return baseTrademarkMapper.selectList(null);
    }

    /**
     * 销售属性列表
     *
     * @return
     */
    @Override
    public List<BaseSaleAttr> listBaseSaleAtt() {
        return baseSaleAttrMapper.selectList(null);
    }

    /**
     * 新增SpuInfo
     *
     * @param spuInfo
     */
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        //判断spuInfo是否为空
        if (spuInfo == null) {
            throw new GmallException("参数不能为空", ResultCodeEnum.FAIL.getCode());
        }
        //无id则保存,有id则修改
        if (spuInfo.getId() == null) {
            //保存
            int insert = spuInfoMapper.insert(spuInfo);
            if (insert <= 0) {
                throw new GmallException("新增SpuInfo失败", ResultCodeEnum.FAIL.getCode());
            }


        } else {
            //修改
            int update = spuInfoMapper.updateById(spuInfo);
            if (update < 0) {
                throw new GmallException("修改SpuInfo失败", ResultCodeEnum.FAIL.getCode());
            }
            Long spuInfoId = spuInfo.getId();
            //删除销售属性
            int delete1 = spuSaleAttrMapper.delete(
                    new LambdaQueryWrapper<SpuSaleAttr>()
                            .eq(SpuSaleAttr::getSpuId, spuInfoId));
            //删除销售属性值
            int delete2 = spuSaleAttrValueMapper.delete(
                    new LambdaQueryWrapper<SpuSaleAttrValue>()
                            .eq(SpuSaleAttrValue::getSpuId, spuInfoId));
            //删除图片数据
            int delete3 = spuImageMapper.delete(
                    new LambdaQueryWrapper<SpuImage>()
                            .eq(SpuImage::getSpuId, spuInfoId));
            //判断是否全部删除成功
            if (delete1 < 0 || delete2 < 0 || delete3 < 0) {
                throw new GmallException("修改SpuInfo失败", ResultCodeEnum.FAIL.getCode());
            }
        }
        Long spuInfoId = spuInfo.getId();
        //保存销售属性名和销售属性值
        saveSpuSaleAttr(spuInfoId,spuInfo.getSpuSaleAttrList());

        //保存spuImage
        saveSpuImage(spuInfoId,spuInfo.getSpuImageList());

    }

    /**
     * 根据第三级分类id分页查询SpuInfo
     *
     * @param page
     * @param size
     * @param category3Id
     * @return
     */
    @Override
    public IPage<SpuInfo> pageSpuInfo(Integer page, Integer size, Long category3Id) {
        return spuInfoMapper.selectPage(
                new Page<SpuInfo>(page, size),
                new LambdaQueryWrapper<SpuInfo>()
                        .eq(SpuInfo::getCategory3Id, category3Id)
        );
    }

    /**
     * 保存spu销售属性名和值
     *
     * @param spuInfoId
     * @param spuSaleAttrList
     */
    private void saveSpuSaleAttr(Long spuInfoId, List<SpuSaleAttr> spuSaleAttrList) {
        spuSaleAttrList.stream().forEach(spuSaleAttr -> {
            //设置spuId
            spuSaleAttr.setSpuId(spuInfoId);
            //新增销售属性
            int insert = spuSaleAttrMapper.insert(spuSaleAttr);
            if (insert <= 0) {
                throw new GmallException("新增Spu销售属性名称失败", ResultCodeEnum.FAIL.getCode());
            }
            //新增属性值
            saveSpuSaleAttrValue(spuInfoId,spuSaleAttr.getSaleAttrName(),spuSaleAttr.getSpuSaleAttrValueList());
        });
    }

    /**
     * 新增spu销售属性值
     * @param spuInfoId
     * @param saleAttrName
     * @param spuSaleAttrValueList
     */
    private void saveSpuSaleAttrValue(Long spuInfoId, String saleAttrName, List<SpuSaleAttrValue> spuSaleAttrValueList) {

        spuSaleAttrValueList.stream().forEach(spuSaleAttrValue -> {
            //设置spuId
            spuSaleAttrValue.setSpuId(spuInfoId);
            //设置销售属性名
            spuSaleAttrValue.setSaleAttrName(saleAttrName);
            int insert1 = spuSaleAttrValueMapper.insert(spuSaleAttrValue);
            if (insert1 <= 0) {
                throw new GmallException("新增Spu销售属性值失败", ResultCodeEnum.FAIL.getCode());
            }
        });


    }

    /**
     * 新增spu图片
     * @param spuInfoId
     * @param spuImageList
     */
    private void saveSpuImage(Long spuInfoId, List<SpuImage> spuImageList) {
        spuImageList.forEach(spuImage -> {
            //设置spuId
            spuImage.setSpuId(spuInfoId);
            int insert = spuImageMapper.insert(spuImage);
            if (insert <= 0) {
                throw new GmallException("新增Spu图片失败", ResultCodeEnum.FAIL.getCode());
            }
        });
    }
}
