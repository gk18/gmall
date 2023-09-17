package com.king.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.gmall.model.product.SpuSaleAttr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * ClassName: SpuSaleAttrMapper
 * Package: com.king.gmall.product.mapper
 * @author GK
 * @date 2023/9/16 22:03
 * @description 销售属性表映射
 * @version 1.0
 */
@Mapper
public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {
    /**
     * 根据spuId查询spu销售属性表和销售属性值表
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> selectSpuSaleAttrList(@Param("spuId") Long spuId);
}
