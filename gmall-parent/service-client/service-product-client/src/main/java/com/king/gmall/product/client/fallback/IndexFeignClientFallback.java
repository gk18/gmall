package com.king.gmall.product.client.fallback;

import com.alibaba.fastjson.JSONObject;
import com.king.gmall.product.client.IndexFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/***
 * ClassName: IndexFeignClientFallback
 * Package: com.king.gmall.product.client.fallback
 * @author GK
 * @date 2023/9/22 20:54
 * @description
 * @version 1.0
 */
@Component
public class IndexFeignClientFallback implements IndexFeignClient {
    /**
     * 首页分类数据构建
     *
     * @return
     */
    @Override
    public List<JSONObject> getIndexCategory() {
        return null;
    }
}
