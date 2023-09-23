package com.king.gmall.list.dao;

import com.king.gmall.model.list.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/***
 * ClassName: goodsDao
 * Package: com.king.gmall.list.dao
 * @author GK
 * @date 2023/9/23 18:50
 * @description es商品对象的dao接口
 * @version 1.0
 */
@Repository
public interface GoodsDao extends ElasticsearchRepository<Goods,Long> {
}
