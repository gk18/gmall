package com.king.gmall.all.controller;

import com.king.gmall.all.util.Page;
import com.king.gmall.list.client.ListFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    @Value("${item.url}")
    private String itemUrl;

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
        //排序url存入
        String sortUrl = getSortUrl(searchData);
        model.addAttribute("sortUrl", sortUrl);
        //获取商品数
        Object total = searchResult.get("total");
        //分页工具初始化
        Page pageInfo = new Page<>(Long.valueOf(total.toString()), getPage(searchData.get("pageNum")), 50);
        model.addAttribute("pageInfo", pageInfo);
        //商品详情路径
        model.addAttribute("itemUrl",itemUrl);
        //跳转搜索页
        return "list";
    }

    /**
     * 获取页码
     *
     * @param page
     * @return
     */
    private int getPage(String page) {
        try {
            Integer pageNum = Integer.valueOf(page);
            return pageNum > 0 ? pageNum : 1;

        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 拼接排序url(不拼接排序字段)
     *
     * @param searchData
     * @return
     */
    private String getSortUrl(Map<String, String> searchData) {
        //初始排序url
        StringBuffer sortUrl = new StringBuffer("/search/list?");
        searchData.entrySet().stream().forEach(entry -> {
            //遍历键值做拼接,不拼接排序参数
            //获取键值
            String key = entry.getKey();
            String value = entry.getValue();
            if (!key.equals("sortField") && !key.equals("sortRule")) {
                sortUrl
                        .append(key)
                        .append("=")
                        .append(value)
                        .append("&");
            }
        });
        return sortUrl.substring(0, sortUrl.length() - 1);
    }

    /**
     * 获取查询url
     *
     * @param searchData
     * @return
     */
    private String getUrl(Map<String, String> searchData) {
        //初始url
        StringBuffer url = new StringBuffer("/search/list?");
        searchData.entrySet().stream().forEach(entry -> {
            //获取键值
            String key = entry.getKey();
            String value = entry.getValue();
            //做拼接,不拼接分页字段
            if (!key.equals("pageNum")) {
                url
                        .append(key)
                        .append("=")
                        .append(value)
                        .append("&");
            }
        });
        return url.substring(0, url.length() - 1);
    }
}
