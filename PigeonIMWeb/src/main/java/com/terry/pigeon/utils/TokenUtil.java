package com.terry.pigeon.utils;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-12 20:32
 **/
@Slf4j
public class TokenUtil {
    public static String getUserId(HttpServletRequest request){
        //获取请求头中的的token
        String token = request.getHeader("authorization").replace("Bearer ","");
        //解析token
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            log.error("token解析失败");
            e.printStackTrace();
        }
        //获取用户Id
        String userId = claims.getSubject();
        return userId;
    }
}
