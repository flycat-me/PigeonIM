package com.terry.pigeon.handler;

import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-07 22:58
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemException(SystemException e){
        log.error("系统出现了错误:{}",e.getMsg());
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseResult runtimeExceptionHandler(Exception e){
        log.error("运行时异常！ {}",e.getMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }

    /**
     * 参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseResult<?> processValidException(IllegalArgumentException e) {
        String errorMessage = String.join("; ", e.getMessage());
        log.error(e.toString() + "_" + e.getMessage(), e);
        return ResponseResult.errorResult(AppHttpCodeEnum.FILE_UPLOAD_ERROR.getCode(),errorMessage);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult MethodHandler(MethodArgumentNotValidException e) {

        BindingResult result = e.getBindingResult();
        ObjectError objectError = result.getAllErrors().stream().findFirst().get();

        log.error("实体校验异常：----------------{}", objectError.getDefaultMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,objectError.getDefaultMessage());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseResult HttpMessageHandler(HttpMessageNotReadableException e) {

        log.error("实体校验异常：----------------{}", e.getMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN.getCode(),"请求参数不能为空");
    }
}
