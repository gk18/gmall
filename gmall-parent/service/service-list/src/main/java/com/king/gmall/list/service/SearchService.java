package com.king.gmall.list.service;

import java.io.IOException;
import java.util.Map;

/***
 * ClassName: SearchService
 * Package: com.king.gmall.list.service
 * @author GK
 * @date 2023/9/24 11:40
 * @description 商品搜索接口
 * @version 1.0
 */
public interface SearchService {
    /**
     * 查询商品
     * @param searchData
     * @return
     */
    Map<String,Object> search(Map<String,String> searchData) throws IOException;
}
