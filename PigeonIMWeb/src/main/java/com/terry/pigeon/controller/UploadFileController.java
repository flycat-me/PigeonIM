package com.terry.pigeon.controller;

import com.terry.pigeon.annotation.UploadFileCheck;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.service.ObsUploadService;
import com.terry.pigeon.vo.frontData.FileUploadInitialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-25 21:01
 **/
@RestController
@RequestMapping(value = "/api/upload")
public class UploadFileController {

    @Autowired
    private ObsUploadService obsUploadService;

    @PostMapping("/avatar")
    //@UploadFileCheck(message = "不支持的文件格式", supportedSuffixes = {"png", "jpg",  "jpeg"},supportedFileTypes = {FileType.PNG,FileType.JPEG,FileType.JPEG})//只校验文件后缀
    //同时校验文件后缀和文件头
    @UploadFileCheck(message = "不支持的文件格式"
            ,type = UploadFileCheck.CheckType.SUFFIX
            ,supportedSuffixes = {"png", "jpg",  "jpeg"})
    public ResponseResult uploadPicture(@RequestParam(value = "file") MultipartFile file) throws IOException {
        return obsUploadService.uploadPicture(file);
    }

    @PostMapping("/multipart/initiate")
    public ResponseResult upLoadFileInitiate(@RequestBody FileUploadInitialization init){
        return obsUploadService.upLoadInitiate(init);
    }


    @PostMapping("/multipart")
    public ResponseResult uploadFileMultipart(@RequestParam MultipartFile file,@RequestParam String uploadId,@RequestParam String splitIndex,@RequestParam String splitNum){
        return obsUploadService.uploadFileMultipart(file,uploadId,splitIndex,splitNum);
    }
}
