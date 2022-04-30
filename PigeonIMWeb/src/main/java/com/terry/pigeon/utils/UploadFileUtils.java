package com.terry.pigeon.utils;

import com.obs.services.ObsClient;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-29 12:02
 **/
@Slf4j
public class UploadFileUtils {


    private static List<PartEtag> partEtags = new ArrayList<>();
    /**
     * 文件分片上传初始化
     * @param objectName 文件上传的文件名
     * @return
     */
    public static String initializeShard(String objectName){
        ObsClient obsClient = ObsFileUpload.createObsClient();
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(ObsFileUpload.getBucketName(),objectName);
        InitiateMultipartUploadResult result = null;
        try {
            result = obsClient.initiateMultipartUpload(request);
        }catch (Exception e){
            e.printStackTrace();
            log.error("OBS文件分片初始化失败：{}",e.getMessage());
        }finally {
            if (obsClient != null)
            {
                try
                {
                    obsClient.close();
                }
                catch (IOException e)
                {
                }
            }
        }

        return result.getUploadId();
    }

    //obs分片文件上传
    public static void fragmentationUploadFile(String uploadId, Integer spitIndex, Integer splitNum, String objectName, InputStream inputStream){
        ObsClient obsClient = ObsFileUpload.createObsClient();
        if(spitIndex + 1 == splitNum){

            UploadPartRequest request = new UploadPartRequest();
            request.setBucketName(ObsFileUpload.getBucketName());
            request.setObjectKey(objectName);
            request.setUploadId(uploadId);
            request.setInput(inputStream);
            //request.setOffset(spitIndex * 5 * 1024 * 1204L);
            request.setPartNumber(spitIndex + 1);
            //request.setPartSize(5 * 1024 * 1204L);
            UploadPartResult result = obsClient.uploadPart(request);
            PartEtag partEtag = new PartEtag();
            partEtag.setEtag(result.getEtag());
            partEtag.setPartNumber(result.getPartNumber());
            UploadFileUtils.partEtags.add(partEtag);

            CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest();
            completeRequest.setBucketName(ObsFileUpload.getBucketName());
            completeRequest.setObjectKey(objectName);
            completeRequest.setUploadId(uploadId);
            completeRequest.setPartEtag(UploadFileUtils.partEtags);
            obsClient.completeMultipartUpload(completeRequest);
            partEtags = new ArrayList<>();
        }else {
            UploadPartRequest request = new UploadPartRequest();
            request.setBucketName(ObsFileUpload.getBucketName());
            request.setObjectKey(objectName);
            request.setUploadId(uploadId);
            request.setInput(inputStream);
            //request.setOffset(spitIndex * 5 * 1024 * 1204L);
            request.setPartNumber(spitIndex + 1);
            //request.setPartSize(5 * 1024 * 1204L);
            UploadPartResult result = obsClient.uploadPart(request);
            PartEtag partEtag = new PartEtag();
            partEtag.setEtag(result.getEtag());
            partEtag.setPartNumber(result.getPartNumber());
            partEtags.add(partEtag);
        }
    }
}
