package com.terry.pigeon.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;

import java.io.Serializable;

/**
 * @author terry_lang
 * @description
 * @since 2022-02-28 14:35
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public ResponseResult(){
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.msg = AppHttpCodeEnum.SUCCESS.getMsg();
    }

    public ResponseResult(Integer code,T data){
        this.code = code;
        this.data = data;
    }
    public ResponseResult(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //错误响应
    public static ResponseResult errorResult(int code,String msg){
        ResponseResult result = new ResponseResult();
        return result.error(code,msg);
    }

    public static ResponseResult successResult(){
        ResponseResult result = new ResponseResult();
        return result;
    }

    public static ResponseResult successResult(int code,String msg){
        ResponseResult result = new ResponseResult();
        return result.success(code,null,msg);
    }
    public static ResponseResult successResult(Object data){
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS,AppHttpCodeEnum.SUCCESS.getMsg());
        if (data!=null){
            result.setData(data);
        }
        return result;
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums){
        return setAppHttpCodeEnum(enums,enums.getMsg());
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums,String msg){
        return setAppHttpCodeEnum(enums,msg);
    }

    public static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums){
        return successResult(enums.getCode(), enums.getMsg());
    }

    public static  ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums,String msg){
        return successResult(enums.getCode(),msg);
    }



    public ResponseResult<?> error(Integer code,String msg){
        this.code = code;
        this.msg = msg;
        return this;
    }

    public ResponseResult<?> success(Integer code,T data){
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<?> success(Integer code,T data,String msg){
        this.code = code;
        this.data = data;
        this.msg = msg;
        return this;
    }

    public ResponseResult<?> success(T data){
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
