package com.terry.pigeon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Users;
import com.terry.pigeon.vo.frontData.ChangeEmailDto;
import com.terry.pigeon.vo.frontData.PasswordDto;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户表(Users)表服务接口
 *
 * @author makejava
 * @since 2022-03-02 17:18:46
 */
public interface UsersService extends IService<Users> {

    public Users getUsers();

    ResponseResult getUserInfoById(HttpServletRequest request);

    //查找好友接口
    ResponseResult searchFriend(Users user);

    ResponseResult getUserSetting();

    ResponseResult changePassword(PasswordDto password);

    ResponseResult changeEmail(ChangeEmailDto changeEmailDto);

    ResponseResult changeDetail(Users users);
}
