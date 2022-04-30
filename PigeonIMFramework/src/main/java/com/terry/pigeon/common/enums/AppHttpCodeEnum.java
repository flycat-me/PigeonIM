package com.terry.pigeon.common.enums;

/**
 * @author terry_lang
 * @description
 * @since 2022-02-28 14:42
 **/
public enum AppHttpCodeEnum {
    SUCCESS(200,"操作成功"),
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR(403,"无操作权限"),
    SYSTEM_ERROR(500,"系统错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONE_NUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503,"邮箱已存在"),
    REQUIRE_USERNAME(504,"需要填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    FILE_UPLOAD_ERROR(507,"文件上传出错"),
    USER_NOT_FOUND(508,"该用户不存在，请重新输入"),
    UPDATE_DATABASE_FAIL(509,"更新数据库失败"),
    UPDATE_AVATAR_FAIL(512,"更新头像失败"),
    CREATE_GROUP_FAIL(510,"创建群组失败");
    int code;
    String msg;
    AppHttpCodeEnum(int code,String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }
}
