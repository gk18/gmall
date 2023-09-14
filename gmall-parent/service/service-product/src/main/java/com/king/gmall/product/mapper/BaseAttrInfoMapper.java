package com.king.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.gmall.model.product.BaseAttrInfo;
import org.apache.ibatis.annotations.Mapper;

/***
 * ClassName: BaseAttrInfoMapper
 * Package: com.king.gmall.product.mapper
 * @author GK
 * @date 2023/9/14 19:14
 * @description 平台属性表的Mapper映射
 * @version 1.0
 */
@Mapper
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {
}
