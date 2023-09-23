package com.king.gmall.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.king.gmall.model.product.BaseCategoryView;
import com.king.gmall.product.mapper.BaseCategoryViewMapper;
import com.king.gmall.product.service.IndexService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/***
 * ClassName: IndexServiceImpl
 * Package: com.king.gmall.product.service.impl
 * @author GK
 * @date 2023/9/22 19:51
 * @description 首页使用的接口实现类
 * @version 1.0
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Resource
    private BaseCategoryViewMapper baseCategoryViewMapper;
    /**
     * 构建商城首页的分类层级关系
     *
     * @return
     */
    @Override
    public List<JSONObject> getIndexCategory() {
        //创建返回结果
        List<JSONObject> result = new ArrayList<>();
        //查询所有分类数据
        List<BaseCategoryView> categoryViewList =
                baseCategoryViewMapper.selectList(null);
        //按一级分类id分桶
        Map<Long, List<BaseCategoryView>> category1Map = categoryViewList.stream()
                .collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));
        //遍历一级分类map
        Set<Map.Entry<Long, List<BaseCategoryView>>> entries = category1Map.entrySet();
        for (Map.Entry<Long, List<BaseCategoryView>> category1View : entries) {
            //创建一级分类结果
            JSONObject category1 = new JSONObject();
            //填充一级分类id和name
            category1.put("categoryId", category1View.getKey());
            category1.put("categoryName", category1View.getValue().get(0).getCategory1Name());
            //根据二级分类id分桶
            Map<Long, List<BaseCategoryView>> category2Map = category1View.getValue().stream()
                    .collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
            //二级分类列表
            List<JSONObject> category2List = new ArrayList<>();
            Set<Map.Entry<Long, List<BaseCategoryView>>> category2Entry = category2Map.entrySet();
            for (Map.Entry<Long, List<BaseCategoryView>> category2View : category2Entry) {
                //创建二级分类结果
                JSONObject category2 = new JSONObject();
                //填充二级分类id和name
                category2.put("categoryId", category2View.getKey());
                category2.put("categoryName", category2View.getValue().get(0).getCategory2Name());

                //三级分类列表
                List<JSONObject> category3List = new ArrayList<>();
                //遍历三级分类
                category2View.getValue().stream().forEach(category -> {
                    //创建三级分类结果
                    JSONObject category3 = new JSONObject();
                    category3.put("categoryId", category.getCategory3Id());
                    category3.put("categoryName", category.getCategory3Name());
                    category3List.add(category3);
                });
                category2.put("childCategory", category3List);
                category2List.add(category2);
            }
            category1.put("childCategory",category2List);
            result.add(category1);
        }
        return result;

    }
}
