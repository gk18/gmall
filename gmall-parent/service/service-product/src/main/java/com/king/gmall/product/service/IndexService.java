package com.king.gmall.product.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/***
 * ClassName: IndexService
 * Package: com.king.gmall.product.service
 * @author GK
 * @date 2023/9/22 19:48
 * @description 首页使用的接口类
 * @version 1.0
 */
public interface IndexService {
    /**
     * 构建商城首页的分类层级关系
     * @return
     */
    List<JSONObject> getIndexCategory();
}
