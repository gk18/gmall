package com.king.gmall.product.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.king.gmall.common.result.Result;

import com.king.gmall.model.product.BaseAttrInfo;
import com.king.gmall.model.product.SpuInfo;
import com.king.gmall.product.service.ManageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/***
 * ClassName: ManageController
 * Package: com.king.gmall.product.controller
 * @author GK
 * @date 2023/9/15 15:21
 * @description
 * @version 1.0
 */
@RestController
@RequestMapping("/admin/product")
public class ManageController {
    @Resource
    private ManageService manageService;

    /**
     * 查询一级分类
     *
     * @return
     */
    @GetMapping("/getCategory1")
    public Result getCategory1() {
        return Result.ok(manageService.findBaseCategory1());
    }

    /**
     * 查询二级分类
     *
     * @param category1Id
     * @return
     */
    @GetMapping("/getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable("category1Id") Long category1Id) {
        return Result.ok(manageService.findBaseCategory2(category1Id));
    }

    /**
     * 查询三级分类
     *
     * @param category2Id
     * @return
     */
    @GetMapping("/getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable("category2Id") Long category2Id) {
        return Result.ok(manageService.findBaseCategory3(category2Id));
    }

    /**
     * 保存平台属性
     *
     * @param baseAttrInfo
     * @return
     */
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        manageService.saveOrAlterBaseAttrInfo(baseAttrInfo);
        return Result.ok();
    }

    /**
     * 查询三级分类下的平台属性
     *
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result attrInfoList(@PathVariable("category1Id") Long category1Id,
                               @PathVariable("category2Id") Long category2Id,
                               @PathVariable("category3Id") Long category3Id) {
        return Result.ok(manageService.getBaseAttrInfo(category3Id));
    }

    /**
     * 删除平台属性
     *
     * @param attrId
     * @return
     */
    @DeleteMapping("/deleteAttrInfo/{attrId}")
    public Result deleteAttrInfo(@PathVariable("attrId") Long attrId) {
        manageService.removeBaseAttrInfo(attrId);
        return Result.ok();
    }

    /**
     * 修改平台属性
     *
     * @param baseAttrInfo
     * @return
     */
    @PutMapping("/alterAttrInfo")
    public Result alterAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        manageService.saveOrAlterBaseAttrInfo(baseAttrInfo);
        return Result.ok();
    }

    /**
     * 分页查询品牌
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/baseTrademark/{page}/{size}")
    public Result showBaseTrademark(@PathVariable("page") Integer page,
                                    @PathVariable("size") Integer size) {
        return Result.ok(manageService.pageListBaseTrademark(page, size));
    }

    /**
     * 查询品牌列表
     *
     * @return
     */
    @GetMapping("/baseTrademark/getTrademarkList")
    public Result getTrademarkList() {
        return Result.ok(manageService.listBaseTrademark());
    }

    /**
     * 查询销售属性列表
     *
     * @return
     */
    @GetMapping("/baseSaleAttrList")
    public Result baseSaleAttrList() {
        return Result.ok(manageService.listBaseSaleAtt());
    }

    /**
     * 新增SpuInfo
     *
     * @param spuInfo
     * @return
     */
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo) {
        manageService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    /**
     * 分页展示spuInfo
     * @param page
     * @param size
     * @param category3Id
     * @return
     */
    @GetMapping("/{page}/{size}")
    public Result showSpuInfo(@PathVariable("page") Integer page,
                              @PathVariable("size") Integer size,
                              Long category3Id) {
        return Result.ok(manageService.pageSpuInfo(page, size, category3Id));
    }


}
