package com.king.gmall.oauth.service.impl;

import com.king.gmall.common.execption.GmallException;
import com.king.gmall.common.result.ResultCodeEnum;
import com.king.gmall.oauth.service.LoginService;
import com.king.gmall.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/***
 * ClassName: LoginServiceImpl
 * Package: com.king.gmall.oauth.service.impl
 * @author GK
 * @date 2023/9/27 18:55
 * @description 登录接口实现类
 * @version 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private LoadBalancerClient loadBalancerClient;
    @Value("${auth.clientId}")
    private String clientId;
    @Value("${auth.clientSecret}")
    private String clientSecret;
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 登录方法
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public AuthToken login(String username, String password) {
        //用户名密码判空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new GmallException("用户名密码不能为空", ResultCodeEnum.FAIL.getCode());
        }
        //包装请求参数:平台id,平台密钥,授权类型,用户名,密码
        //包装请求头(平台id,平台密钥)
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", getAuthorization());
        //包装请求体(授权类型,用户名,密码)
        MultiValueMap<String, String> body = new HttpHeaders();
        body.set("grant_type", "password");
        body.set("username", username);
        body.set("password", password);
        HttpEntity<Object> httpEntity = new HttpEntity<>(body,httpHeaders);
        //根据应用名向注册中心获取地址
        ServiceInstance serviceInstance = loadBalancerClient.choose(applicationName);
        //向spring发送请求验证用户名,密码是否正确
        ResponseEntity<Map> response = restTemplate.exchange(
                serviceInstance.getUri().toString() + "/oauth/token",
                HttpMethod.POST,
                httpEntity,
                Map.class);
        //获取结果,解析结果
        Map<String, String> result = response.getBody();
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(result.get("access_token"));
        authToken.setRefreshToken(result.get("refresh_token"));
        authToken.setJti(result.get("jti"));
        return authToken;
    }

    /**
     * 根据平台id和平台密钥生成授权
     *
     * @return
     */
    private String getAuthorization() {
        try {
            String s = clientId + ":" + clientSecret;
            return "Basic " + new String(Base64.getEncoder().encode(s.getBytes()),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
