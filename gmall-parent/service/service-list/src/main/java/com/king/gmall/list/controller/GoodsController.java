package com.king.gmall.list.controller;

import com.king.gmall.common.result.Result;
import com.king.gmall.list.service.GoodsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/***
 * ClassName: GoodsController
 * Package: com.king.gmall.list.controller
 * @author GK
 * @date 2023/9/23 19:09
 * @description
 * @version 1.0
 */

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @GetMapping("/addSkuFromMysqlToEs/{skuId}")
    public Result addSkuFromMysqlToEs(@PathVariable("skuId") Long skuId) {
        goodsService.addSkuFromMysqlToEs(skuId);
        return Result.ok();
    }
    @GetMapping("/removeGoodsFromEs/{skuId}")
    public Result removeGoodsFromEs(@PathVariable("skuId") Long skuId) {
        goodsService.removeGoodsFromEs(skuId);
        return Result.ok();
    }
}




