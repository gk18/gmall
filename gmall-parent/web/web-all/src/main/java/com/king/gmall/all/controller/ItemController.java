package com.king.gmall.all.controller;

import com.king.gmall.common.result.Result;
import com.king.gmall.item.client.ItemFeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/***
 * ClassName: ItemController
 * Package: com.king.gmall.all.controller
 * @author GK
 * @date 2023/9/18 20:58
 * @description
 * @version 1.0
 */
@Controller
@RequestMapping
public class ItemController {
    @Resource
    private ItemFeignClient itemFeignClient;

    /**
     * sku详情页面
     * @param skuId
     * @param model
     * @return
     */
    @RequestMapping("{skuId}.html")
    public String getItem(@PathVariable Long skuId, Model model){
        // 通过skuId 查询skuInfo
        Result<Map> result = itemFeignClient.getSkuDetails(skuId);
        model.addAllAttributes(result.getData());
        return "item/index";
    }
}
