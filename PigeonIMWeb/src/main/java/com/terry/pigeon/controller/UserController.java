package com.terry.pigeon.controller;

import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Users;
import com.terry.pigeon.service.UsersService;
import com.terry.pigeon.vo.frontData.ChangeEmailDto;
import com.terry.pigeon.vo.frontData.PasswordDto;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author terry_lang
 * @description
 * @since 2022-04-16 20:28
 **/
@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Resource
    private UsersService usersService;

    @GetMapping("/setting")
    public ResponseResult getUserSetting(){
        return usersService.getUserSetting();
    }

    @GetMapping("/userInfo")
    public ResponseResult getUserInfo(HttpServletRequest request){
        return usersService.getUserInfoById(request);
    }

    @PostMapping("/change/password")
    public ResponseResult changePassword(@RequestBody PasswordDto password){
        return usersService.changePassword(password);
    }

    @PostMapping("/change/mobile")
    public ResponseResult changeMobile(){
        return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL.getCode(),"不允许修改手机号");
    }

    @PostMapping("/change/email")
    public ResponseResult changeEmail(@RequestBody ChangeEmailDto changeEmailDto){
        return usersService.changeEmail(changeEmailDto);
    }

    @PostMapping("/change/detail")
    public ResponseResult changeDetail(@RequestBody Users users){
        return usersService.changeDetail(users);
    }


}
