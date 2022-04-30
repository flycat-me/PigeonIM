package com.terry.pigeon.service;

import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.vo.frontData.FileMessageDo;
import com.terry.pigeon.vo.frontData.FileUploadInitialization;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-25 21:08
 **/
public interface ObsUploadService {
    ResponseResult uploadPicture(MultipartFile multipartFile) throws IOException;

    ResponseResult sendImageMessage(MultipartFile image,String channelType,String receiverId);

    ResponseResult upLoadInitiate(FileUploadInitialization init);

    ResponseResult uploadFileMultipart(MultipartFile file, String uploadId, String splitIndex, String splitNum);

    ResponseResult sendFileMessage(FileMessageDo fileMessage);
}
