package com.king.gmall.oauth.controller;

import com.king.gmall.common.result.Result;
import com.king.gmall.oauth.service.LoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/***
 * ClassName: LoginController
 * Package: com.king.gmall.oauth.controller
 * @author GK
 * @date 2023/9/27 18:47
 * @description 用户登录控制层
 * @version 1.0
 */
@RestController
@RequestMapping("/login/user")
public class LoginController {
    @Resource
    private LoginService loginService;
    @GetMapping
    public Result login(String username, String password) {
        return Result.ok(loginService.login(username, password));
    }
}
