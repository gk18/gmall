package com.king.gmall.all.controller;

import com.king.gmall.product.client.IndexFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/***
 * ClassName: IndexController
 * Package: com.king.gmall.all.controller
 * @author GK
 * @date 2023/9/22 20:58
 * @description 首页展示控制层
 * @version 1.0
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private IndexFeignClient indexFeignClient;
    @GetMapping
    public String index(Model model) {
        model.addAttribute("categoryList",indexFeignClient.getIndexCategory());
        return "index";
    }
}
