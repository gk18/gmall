package com.king.gmall.all.controller;

import com.king.gmall.list.client.ListFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Map;

/***
 * ClassName: SearchController
 * Package: com.king.gmall.all.controller
 * @author GK
 * @date 2023/9/25 14:04
 * @description 跳转搜索页面
 * @version 1.0
 */
@Controller
@RequestMapping("/search")
public class SearchController {
    @Resource
    private ListFeignClient listFeignClient;

    /**
     * @param model      模板对象
     * @param searchData 展示数据
     * @return
     */
    @GetMapping("/list")
    public String search(Model model, @RequestParam Map<String, String> searchData) {
        //获取搜索结果
        Map<String, Object> searchResult = listFeignClient.search(searchData);
        model.addAllAttributes(searchResult);
        //条件回显
        model.addAttribute("searchData", searchData);
        //搜索url存入
        String url = getUrl(searchData);
        model.addAttribute("url", url);
        //跳转搜索页
        return "list";
    }

    /**
     * 获取查询url
     * @param searchData
     * @return
     */
    private String getUrl(Map<String, String> searchData) {
        //初始url
        StringBuilder url = new StringBuilder("/search/list?");
        searchData.entrySet().stream().forEach(entry -> {
            //遍历键值做拼接
            url
                    .append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        });
        return url.substring(0, url.length() - 1);
    }
}
