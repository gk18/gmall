package com.king.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.gmall.model.product.SkuSaleAttrValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/***
 * ClassName: SkuSaleAttrValueMapper
 * Package: com.king.gmall.product.mapper
 * @author GK
 * @date 2023/9/17 14:47
 * @description Sku销售属性值表映射
 * @version 1.0
 */
@Mapper
public interface SkuSaleAttrValueMapper extends BaseMapper<SkuSaleAttrValue> {
    /**
     * 查询指定spu的sku所有销售属性键值对
     * @param spuId
     * @return
     */
    List<Map> selectSkuMap(@Param("spuId") Long spuId);
}
