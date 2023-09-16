package com.king.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.gmall.model.product.BaseTrademark;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/***
 * ClassName: BaseTradmarkMapper
 * Package: com.king.gmall.product.mapper
 * @author GK
 * @date 2023/9/16 14:52
 * @description 品牌映射表
 * @version 1.0
 */
@Mapper
public interface BaseTrademarkMapper extends BaseMapper<BaseTrademark> {

}
