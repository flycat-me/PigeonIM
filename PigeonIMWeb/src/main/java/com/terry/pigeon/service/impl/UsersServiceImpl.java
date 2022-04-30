package com.terry.pigeon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.common.utils.BeanCopyUtils;
import com.terry.pigeon.entity.Users;
import com.terry.pigeon.mapper.UsersMapper;
import com.terry.pigeon.service.UsersService;
import com.terry.pigeon.utils.RedisCache;
import com.terry.pigeon.utils.TokenUtil;
import com.terry.pigeon.vo.UserInfoVo;
import com.terry.pigeon.vo.frontData.ChangeEmailDto;
import com.terry.pigeon.vo.frontData.PasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 用户表(Users)表服务实现类
 *
 * @author makejava
 * @since 2022-03-02 17:18:47
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Resource
    UsersMapper usersMapper;

    @Autowired
    RedisCache redisCache;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Users getUsers() {
        return usersMapper.getByUserIdString();
    }

    @Override
    public ResponseResult getUserInfoById(HttpServletRequest request) {
        String userId = TokenUtil.getUserId(request);
        //通过用户Id在redis中查询用户信息
        Users users = redisCache.getCacheObject(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        //使用Bean拷贝工具将用户信息进行拷贝
        userInfoVo = BeanCopyUtils.copyBean(users,UserInfoVo.class);
        return ResponseResult.successResult(userInfoVo);
    }

    @Override
    public ResponseResult searchFriend(Users user) {
        Users searchUser = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getMobile, user.getMobile()));
        if (!Objects.isNull(searchUser)){
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo = BeanCopyUtils.copyBean(searchUser,UserInfoVo.class);
            return ResponseResult.successResult(userInfoVo);
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_NOT_FOUND);
        }

    }

    @Override
    public ResponseResult getUserSetting() {

        Users users = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUserId, FriendsServiceImpl.getCurUser()
                                                                                                                   .getUserId()));
        UserInfoVo userSetting = BeanCopyUtils.copyBean(users,UserInfoVo.class);
        return ResponseResult.successResult(userSetting);
    }

    @Override
    public ResponseResult changePassword(PasswordDto password) {

        //先查询旧密码是否正确
        Users users = FriendsServiceImpl.getCurUser();
        String oldPassword = users.getPassword();
        String newPassword = password.getNewPassword();
        if (passwordEncoder.matches(password.getOldPassword(),oldPassword)) {
            users.setPassword(passwordEncoder.encode(newPassword));
            int i = usersMapper.updateById(users);
            redisCache.setCacheObject(users.getUserId().toString(),users);
            return i > 0 ? ResponseResult.successResult() : ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
    }

    @Override
    public ResponseResult changeEmail(ChangeEmailDto changeEmailDto) {

        Users users = FriendsServiceImpl.getCurUser();
        if (passwordEncoder.matches(changeEmailDto.getPassword(),users.getPassword())) {
            users.setEmail(changeEmailDto.getEmail());
            int i = usersMapper.updateById(users);
            redisCache.setCacheObject(users.getUserId().toString(),users);
            return i > 0 ? ResponseResult.successResult() : ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
    }

    @Override
    public ResponseResult changeDetail(Users users) {

        Users curUser = FriendsServiceImpl.getCurUser();
        curUser.setAvatar(users.getAvatar());
        curUser.setGender(users.getGender());
        curUser.setMotto(users.getMotto());
        curUser.setNickName(users.getNickName());

        redisCache.setCacheObject(curUser.getUserId().toString(),curUser);

        int i = usersMapper.updateById(curUser);

        return i > 0 ? ResponseResult.successResult() : ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
    }
}
