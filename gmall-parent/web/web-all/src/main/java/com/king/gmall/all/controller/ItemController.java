package com.king.gmall.all.controller;

import com.king.gmall.common.result.Result;
import com.king.gmall.item.client.ItemFeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.*;
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
@RequestMapping("/item")
public class ItemController {
    @Resource
    private ItemFeignClient itemFeignClient;
    @Resource
    private TemplateEngine templateEngine;

    /**
     * sku详情页面
     * @param skuId
     * @param model
     * @return
     */
    @RequestMapping
    public String getItem(Long skuId, Model model){
        // 通过skuId 查询skuInfo
        Result<Map> result = itemFeignClient.getSkuDetails(skuId);
        model.addAllAttributes(result.getData());
        return "item";
    }

    /**
     * 为指定商品生成静态页面
     *
     * @param skuId
     *
     * @return
     */
    @GetMapping("/createSkuHtml")
    @ResponseBody
    public String createSkuHtml(Long skuId) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter printWriter = null;
        try {
            //创建存放数据的容器
            Context context = new Context();
            //远程调用
            Result<Map> result = itemFeignClient.getSkuDetails(skuId);
            //往容器放入数据
            context.setVariables(result.getData());
            //创建数据输出流
            printWriter = new PrintWriter("E:/code/电商静态页面/" + skuId + ".html", "UTF-8");
            //生成模板页面
            templateEngine.process("item-template", context, printWriter);

            return "为skuId为" + skuId + "的商品生成静态页面成功!";
        } finally {
            if(printWriter != null) {
                //刷新并关闭流
                printWriter.flush();
                printWriter.close();
            }
        }
    }
}
