package com.terry.pigeon.controller;

import com.terry.pigeon.common.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author terry_lang
 * @description
 * @since 2022-04-06 16:36
 **/
@RestController
@RequestMapping("/api/emoticon")
public class EmoticonController {

    @GetMapping("/list")
    public ResponseResult getEmoticon(){
        return ResponseResult.successResult();
    }

    @GetMapping("/system/list")
    public ResponseResult getSystemEmoticon(){
        return ResponseResult.successResult();
    }
}
