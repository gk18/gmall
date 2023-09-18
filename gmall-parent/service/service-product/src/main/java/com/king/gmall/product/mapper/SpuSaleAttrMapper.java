package com.king.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.gmall.model.product.SpuSaleAttr;
import com.king.gmall.model.product.SpuSaleAttrValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

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
     *
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> selectSpuSaleAttrList(@Param("spuId") Long spuId);

    /**
     * 根据spuId和skuId查询销售属性
     *
     * @param skuId
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> spuSaleAttr(@Param("skuId") Long skuId,
                                  @Param("spuId") Long spuId);
}
