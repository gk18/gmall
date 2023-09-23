package com.king.gmall.all.controller;

import com.king.gmall.all.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/***
 * ClassName: TestTyhmeleafController
 * Package: com.king.gmall.all.controller
 * @author GK
 * @date 2023/9/22 14:10
 * @description 测试thymeleaf控制层
 * @version 1.0
 */
@Controller
@RequestMapping("/test")
public class TestThymeleafController {
    @GetMapping
    public String test(Model model) {
        //存入字符串
        model.addAttribute("description", "这是一个Thymeleaf的测试用例");
        model.addAttribute("html", "<h1>这是一个Thymeleaf的测试用例</h1>");
        //存入列表
        List<User> userList = new ArrayList<User>();
        for (long i = 1; i <= 9; i++) {
            userList.add(new User(i, "king" + i, "king" + i + "@qq.com"));
        }
        model.addAttribute("userList", userList);
        //存入map集合
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "A");
        map.put("b", "B");
        map.put("c", "C");
        model.addAttribute("map", map);
        //存入当前时间
        model.addAttribute("date", new Date());
        //存入图片路径
        model.addAttribute("imgUrl", "https://p6.itc.cn/q_70/images01/20230108/a03510f476294177894559f2f1778621.jpeg");
        //存储url
        model.addAttribute("baidu", "/test/baidu");
        model.addAttribute("douyin", "/test/douyin");
        return "test";
    }

    @GetMapping("/baidu")
    public String goToBaidu(String name, String email) {
        System.out.println(new User(1L, name, email));
        return "redirect:http://www.baidu.com";
    }
    @GetMapping("/douyin")
    public String goToDouyin(String name, String email) {
        System.out.println(new User(2L, name, email));
        return "redirect:http://www.douyin.com";

    }
}
