package com.terry.pigeon.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.terry.pigeon.entity.LoginUser;
import com.terry.pigeon.entity.Users;
import com.terry.pigeon.mapper.UsersMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-09 15:38
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UsersMapper usersMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getMobile,username);
        Users user = usersMapper.selectOne(queryWrapper);
        //判断是否查询到用户，如果查询不到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        return new LoginUser(user);
    }
}
