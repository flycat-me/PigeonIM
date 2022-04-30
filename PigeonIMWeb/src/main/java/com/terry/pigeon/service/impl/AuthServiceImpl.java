package com.terry.pigeon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.terry.pigeon.common.constants.SystemConstants;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.LoginUser;
import com.terry.pigeon.entity.Users;
import com.terry.pigeon.mapper.UsersMapper;
import com.terry.pigeon.service.AuthService;
import com.terry.pigeon.utils.JwtUtil;
import com.terry.pigeon.utils.RedisCache;
import com.terry.pigeon.vo.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-09 14:27
 **/
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Resource
    private UsersMapper usersMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult login(Users user) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getMobile(),user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //判断是否通过认证
        if (Objects.isNull(authentication)){
            throw new RuntimeException("认证失败");
        }
        //获取UserId生成token
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getUserId().toString();
        String jwt = JwtUtil.createJWT(userId,JwtUtil.JWT_TTL);
        //将当前登录用户储存到Redis
        redisCache.setCacheObject(userId,loginUser.getUser(),SystemConstants.USER_TOKEN_EXPIRES, TimeUnit.SECONDS);



        return ResponseResult.successResult(new UserLoginVo(jwt, SystemConstants.USER_TOKEN_EXPIRES));
    }

    @Override
    public ResponseResult userRegister(Users user) {
        Users regUser = new Users();
        //通过Mybatis-Plus中的lambda条件构造器构造查询条件
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Users::getMobile,user.getMobile());
        //判断用户填写手机号，是否已经被注册
        if (!Objects.isNull(usersMapper.selectOne(queryWrapper))){
            return ResponseResult.errorResult(AppHttpCodeEnum.PHONE_NUMBER_EXIST);
        }
        regUser.setMobile(user.getMobile());
        regUser.setNickName(user.getNickName());
        //设置用户密码，通过passwordEncoder进行加密处理
        regUser.setPassword(passwordEncoder.encode(user.getPassword()));
        regUser.setCreatedAt(new Date());
        //将用户添加到数据库中
        int insert = usersMapper.insert(regUser);
        if (insert > 0){
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.FILE_UPLOAD_ERROR.getCode(),"注册失败");
        }

    }

    @Override
    public ResponseResult userLogout(HttpServletRequest request) {
        //String userId = TokenUtil.getUserId(request);
        //在SpringSecurity中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users loginUser = (Users) authentication.getPrincipal();
        //删除redis中的用户信息
        redisCache.deleteObject(loginUser.getUserId().toString());
        return ResponseResult.successResult("退出成功");

    }
}
