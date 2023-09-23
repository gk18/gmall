package com.king.gmall.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.king.gmall.product.service.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/***
 * ClassName: IndexController
 * Package: com.king.gmall.product.controller
 * @author GK
 * @date 2023/9/22 20:30
 * @description 首页使用的控制层
 * @version 1.0
 */
@RestController
@RequestMapping("/index/product")
public class IndexController {
    @Resource
    private IndexService indexService;
    /**
     * 首页分类数据构建
     * @return
     */
    @GetMapping("/getIndexCategory")
    public List<JSONObject> getIndexCategory() {
        return indexService.getIndexCategory();
    }
}
