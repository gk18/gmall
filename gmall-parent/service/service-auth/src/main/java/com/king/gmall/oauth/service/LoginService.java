package com.king.gmall.oauth.service;

import com.king.gmall.oauth.util.AuthToken;

/***
 * ClassName: LoginService
 * Package: com.king.gmall.oauth.service
 * @author GK
 * @date 2023/9/27 18:54
 * @description 登录接口
 * @version 1.0
 */
public interface LoginService {
    /**
     * 登录方法
     *
     * @param userName
     * @param password
     * @return
     */
    AuthToken login(String userName, String password);
}
