package com.king.gmall.list.client.fallback;

import com.king.gmall.list.client.ListFeignClient;
import org.springframework.stereotype.Component;

import java.util.Map;

/***
 * ClassName: ListFeignClientFallback
 * Package: com.king.gmall.list.client.fallback
 * @author GK
 * @date 2023/9/25 15:11
 * @description
 * @version 1.0
 */
@Component
public class ListFeignClientFallback implements ListFeignClient {
    @Override
    public Map<String, Object> search(Map<String, String> searchData) {
        return null;
    }
}
