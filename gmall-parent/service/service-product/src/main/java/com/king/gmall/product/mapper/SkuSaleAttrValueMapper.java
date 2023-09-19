package com.king.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.gmall.model.product.SkuSaleAttrValue;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    @MapKey("sku_id")
    List<Map> selectSkuMap(@Param("spuId") Long spuId);
    /**
     * 查询指定spu的sku所有销售属性键值对
     * @param spuId
     * @return
     */
    @Select("SELECT\n" +
            "\tsku_id,\n" +
            "\tGROUP_CONCAT(DISTINCT sale_attr_value_id ORDER BY sale_attr_value_id SEPARATOR ',' ) value_id\n" +
            "FROM\n" +
            "\tsku_sale_attr_value\n" +
            "WHERE \n" +
            "\tspu_id = #{spuId}\n" +
            "GROUP BY\n" +
            "\tsku_id")
    List<Map> selectSkuSaleValueId(@Param("spuId") Long spuId);
}
