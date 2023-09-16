package com.king.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.gmall.model.product.BaseAttrValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * ClassName: BaseAttrValueMapper
 * Package: com.king.gmall.product.mapper
 * @author GK
 * @date 2023/9/15 15:55
 * @description 平台属性值映射
 * @version 1.0
 */
@Mapper
public interface BaseAttrValueMapper extends BaseMapper<BaseAttrValue> {
    int batchInsert(@Param("list") List<BaseAttrValue> attrValueList);
}
