package com.terry.pigeon.handler;

import com.terry.pigeon.common.enums.AppHttpCodeEnum;

/**
 * @author terry_lang
 * @description 自定义异常处理类
 * @since 2022-03-07 22:58
 **/
public class SystemException extends RuntimeException{

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    public SystemException(AppHttpCodeEnum httpCodeEnum){
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}
