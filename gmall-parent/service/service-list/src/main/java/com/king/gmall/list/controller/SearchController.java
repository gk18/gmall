package com.king.gmall.list.controller;

import com.king.gmall.list.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;


/***
 * ClassName: SearchController
 * Package: com.king.gmall.list.controller
 * @author GK
 * @date 2023/9/24 11:39
 * @description 搜索控制层
 * @version 1.0
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {
    @Resource
    private SearchService searchService;
    @GetMapping
    public Map<String, Object> search(@RequestParam Map<String, String> searchData) throws IOException {
        return searchService.search(searchData);
    }
}
