package com.king.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.gmall.model.product.SkuInfo;
import org.apache.ibatis.annotations.Mapper;

/***
 * ClassName: SkuInfoMapper
 * Package: com.king.gmall.product.mapper
 * @author GK
 * @date 2023/9/17 14:43
 * @description sku表映射
 * @version 1.0
 */
@Mapper
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {
}
