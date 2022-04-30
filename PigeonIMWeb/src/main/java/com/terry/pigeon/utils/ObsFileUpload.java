package com.terry.pigeon.utils;

import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-25 21:41
 **/
@Component
@ConfigurationProperties(prefix = "pigeon.im.obs")
//@Data
@Slf4j
public class ObsFileUpload {
    private static String endPoint;
    private static String ak;
    private static String sk;
    private static String bucketName;

    private static List<PartEtag> partEtags;


    public static ObsClient createObsClient(){
        ObsConfiguration config = new ObsConfiguration();
        config.setEndPoint(endPoint);
        config.setSocketTimeout(30000);
        config.setMaxErrorRetry(1);
        return new ObsClient(ak,sk,config);
    }

    public String uploadFileToObs(String objectKey, InputStream InputStream) {
        ObsClient obsClient = createObsClient();

        String filePath = UploadPathUtils.generateFilePath(objectKey);
        PutObjectResult putObjectResult = null;
        try {
            putObjectResult = obsClient.putObject(bucketName, filePath, InputStream);
        } catch (Exception e) {
            log.error("obs上传文件失败");
            e.printStackTrace();
        }

        return "https://pigeon-im.obs.cn-east-3.myhuaweicloud.com:443/"+filePath;
    }

    public String uploadImageToObs(String objectKey, MultipartFile multipartFile) throws IOException {
        ObsClient obsClient = createObsClient();


        //获取图片文件的大小
        BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
        int with = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        String filePath = UploadPathUtils.generateImageFilePath(objectKey,with,height);
        PutObjectResult putObjectResult = null;
        try {
            putObjectResult = obsClient.putObject(bucketName, filePath, multipartFile.getInputStream());
        } catch (Exception e) {
            log.error("obs上传文件失败");
            e.printStackTrace();
        }finally
        {
            if (obsClient != null)
            {
                try
                {
                    /*
                     * Close obs client
                     */
                    obsClient.close();
                }
                catch (IOException e)
                {
                }
            }
        }


        return "https://pigeon-im.obs.cn-east-3.myhuaweicloud.com:443/"+filePath;
    }


    public void setEndPoint(String endPoint) {
        ObsFileUpload.endPoint = endPoint;
    }

    public void setAk(String ak) {
        ObsFileUpload.ak = ak;
    }

    public void setSk(String sk) {
        ObsFileUpload.sk = sk;
    }

    public void setBucketName(String bucketName) {
        ObsFileUpload.bucketName = bucketName;
    }

    public static String getBucketName() {
        return bucketName;
    }
}
