package com.king.gmall.item.controller;

import com.king.gmall.common.result.Result;
import com.king.gmall.item.service.ItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

/***
 * ClassName: ItemController
 * Package: com.king.gmall.item.controller
 * @author GK
 * @date 2023/9/18 18:45
 * @description 商品详情服务控制层
 * @version 1.0
 */
@RestController
@RequestMapping("/gmall/item")
public class ItemController {
    @Resource
    private ItemService itemService;
    @GetMapping("/getSkuDetails")
    public Result getSkuDetails(@RequestParam("skuId") Long skuId) {
        return Result.ok(itemService.getSkuDetails(skuId));
    }
}
