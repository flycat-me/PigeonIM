package com.terry.pigeon.service;

import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Users;

import javax.servlet.http.HttpServletRequest;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-09 14:23
 **/
public interface AuthService {
    ResponseResult login(Users user);

    ResponseResult userRegister(Users user);

    ResponseResult userLogout(HttpServletRequest request);
}
