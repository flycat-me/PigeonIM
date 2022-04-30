package com.terry.pigeon.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-25 21:23
 **/
public class UploadPathUtils {
    public static String generateFilePath(String fileName){
        //根据日期生成路径，2022/3/25/
        SimpleDateFormat sdf = new SimpleDateFormat("yyy/MM/dd/");
        String filePath = sdf.format(new Date());

        //使用uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-","");

        //获取文件后缀
        int index = fileName.lastIndexOf(".");
        String fileType = fileName.substring(index);



        return new StringBuilder().append(filePath).append(uuid).append(fileType).toString();
    }

    public static String generateImageFilePath(String fileName,int width,int height) {

        //根据日期生成路径，2022/3/25/
        SimpleDateFormat sdf = new SimpleDateFormat("yyy/MM/dd/");
        String filePath = sdf.format(new Date());

        //使用uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-","");

        String dimension = "_" + width + "x" + height;

        //获取文件后缀
        int index = fileName.lastIndexOf(".");
        String fileType = fileName.substring(index);

        return new StringBuilder().append(filePath).append(uuid).append(dimension).append(fileType).toString();
    }
}
