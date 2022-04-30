package com.terry.pigeon.security;

import com.alibaba.fastjson.JSON;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Users;
import com.terry.pigeon.utils.JwtUtil;
import com.terry.pigeon.utils.RedisCache;
import com.terry.pigeon.utils.WebUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-09 17:35
 **/
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String bareToken = request.getHeader("authorization");
        if (!StringUtils.hasText(bareToken)){
            filterChain.doFilter(request,response);
            return;
        }
        String token = bareToken.replace("Bearer ","");
//        System.out.println(token);
        //解析获取userId

        try {
            if (JwtUtil.isTokenExpired(token)){
                ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN.getCode(),"token失效");
                WebUtils.renderString(response, JSON.toJSONString(result));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时或token非法
            //响应前端需要重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //获取UserId
        String userId = claims.getSubject();

        //从redis中获取用户信息
        Users loginUser = redisCache.getCacheObject(userId);
        if (Objects.isNull(loginUser)){
            //token过期，需要重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response,JSON.toJSONString(result));
            return;
        }

        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request,response);
    }
}
