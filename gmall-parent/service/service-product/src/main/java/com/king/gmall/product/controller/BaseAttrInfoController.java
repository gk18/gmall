package com.king.gmall.product.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.king.gmall.common.result.Result;
import com.king.gmall.model.product.BaseAttrInfo;
import com.king.gmall.product.service.BaseAttrInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/***
 * ClassName: BaseAttrInfoController
 * Package: com.king.gmall.product.controller
 * @author GK
 * @date 2023/9/14 19:23
 * @description 平台属性管理的控制层
 * @version 1.0
 */
@RestController
@RequestMapping("/attrInfo")
public class BaseAttrInfoController {
    @Resource
    private BaseAttrInfoService baseAttrInfoService;

    /**
     * 查询所有数据
     *
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll() {
        return Result.ok(baseAttrInfoService.findAll());
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @GetMapping("/getOne/{id}")
    public Result getOne(@PathVariable("id") Long id) {
        return Result.ok(baseAttrInfoService.getById(id));
    }

    /**
     * 新增
     *
     * @param baseAttrInfo
     * @return
     */
    @PostMapping("/create")
    public Result create(@RequestBody BaseAttrInfo baseAttrInfo) {
        baseAttrInfoService.create(baseAttrInfo);
        return Result.ok();
    }

    /**
     * 修改
     *
     * @param baseAttrInfo
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody BaseAttrInfo baseAttrInfo) {
        baseAttrInfoService.update(baseAttrInfo);
        return Result.ok();
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        baseAttrInfoService.deleteById(id);
        return Result.ok();
    }

    /**
     * 分页查询
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/page/{current}/{size}")
    public Result page(@PathVariable("current") Integer current,
                       @PathVariable("size") Integer size) {
        return Result.ok(baseAttrInfoService.page(current, size));
    }

    /**
     * 条件查询
     * @param baseAttrInfo
     * @return
     */
    @PostMapping("/search")
    public Result search(@RequestBody BaseAttrInfo baseAttrInfo) {
        return Result.ok(baseAttrInfoService.search(baseAttrInfo));
    }

    /**
     * 分页条件查询
     * @param current
     * @param size
     * @param baseAttrInfo
     * @return
     */
    @PostMapping("/page/{current}/{size}")
    public Result page(@PathVariable("current") Integer current,
                       @PathVariable("size") Integer size,
                       @RequestBody BaseAttrInfo baseAttrInfo) {
        return Result.ok(baseAttrInfoService.page(baseAttrInfo, current, size));
    }
}
