package com.terry.pigeon.controller;

import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Users;
import com.terry.pigeon.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author terry_lang
 * @description 用户登录注册接口实现
 * @since 2022-03-09 14:17
 **/
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    AuthService authService;

    /**
     * 用户登录接口
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseResult userLogin(@Validated  @RequestBody Users user){
        return authService.login(user);
    }

    /**
     * 用户注册接口
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseResult userRegister(@RequestBody Users user){
        return authService.userRegister(user);
    }

    /**
     * 用户退出接口
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public ResponseResult userLogout(HttpServletRequest request){
        return authService.userLogout(request);
    }
}
