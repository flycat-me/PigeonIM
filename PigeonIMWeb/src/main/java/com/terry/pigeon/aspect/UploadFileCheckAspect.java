package com.terry.pigeon.aspect;

import cn.hutool.core.io.FileUtil;
import com.terry.pigeon.annotation.UploadFileCheck;
import com.terry.pigeon.common.enums.FileType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @description 文件校验切面
 */
@Component
@Slf4j
@Aspect
public class UploadFileCheckAspect {

    //切入点配置自定义注解
    @Pointcut(value = "@annotation(com.terry.pigeon.annotation.UploadFileCheck)")
    public void checkFileUpload(){

    }

    //在执行目标方法前执行的操作，配置自定注解中的参数
    @Before("checkFileUpload() && @annotation(uploadFileCheck)")
    public void before(JoinPoint joinPoint, UploadFileCheck uploadFileCheck) {
        //获取配置中允许的文件后缀
        final String[] suffixes = uploadFileCheck.supportedSuffixes();
        //获取配置中的校验方式
        final UploadFileCheck.CheckType type = uploadFileCheck.type();
        //获取配置中的允许上传的文件类型
        final FileType[] fileTypes = uploadFileCheck.supportedFileTypes();
        final String message = uploadFileCheck.message();
        if (ArrayUtils.isEmpty(suffixes) && ArrayUtils.isEmpty(fileTypes)) {
            return;
        }

        Object[] args = joinPoint.getArgs();
        //文件后缀转成set集合
        Set<String> suffixSet = new HashSet<>(Arrays.asList(suffixes));
        for (FileType fileType : fileTypes) {
            suffixSet.add(fileType.getSuffix());
        }
        //文件类型转成set集合
        Set<FileType> fileTypeSet = new HashSet<>(Arrays.asList(fileTypes));
        for (String suffix : suffixes) {
            fileTypeSet.add(FileType.getBySuffix(suffix));
        }
        //对参数是文件的进行校验
        for (Object arg : args) {
            if (arg instanceof MultipartFile) {
                doCheck((MultipartFile) arg, type, suffixSet, fileTypeSet, message);
            } else if (arg instanceof MultipartFile[]) {
                for (MultipartFile file : (MultipartFile[]) arg) {
                    doCheck(file, type, suffixSet, fileTypeSet, message);
                }
            }
        }
    }

    //根据指定的检查类型对文件进行校验
    private void doCheck(MultipartFile file, UploadFileCheck.CheckType type, Set<String> suffixSet, Set<FileType> fileTypeSet, String message) {
        if (type == UploadFileCheck.CheckType.SUFFIX) {
            doCheckSuffix(file, suffixSet, message);
        } else if (type == UploadFileCheck.CheckType.MAGIC_NUMBER) {
            doCheckMagicNumber(file, fileTypeSet, message);
        } else {
            doCheckSuffix(file, suffixSet, message);
            doCheckMagicNumber(file, fileTypeSet, message);
        }
    }

    //验证文件头信息
    private void doCheckMagicNumber(MultipartFile file, Set<FileType> fileTypeSet, String message) {
        String magicNumber = readMagicNumber(file);
        String fileName = file.getOriginalFilename();
        String fileSuffix = FileUtil.extName(fileName);
        for (FileType fileType : fileTypeSet) {
            if (magicNumber.startsWith(fileType.getMagicNumber()) && fileType.getSuffix().toUpperCase().equalsIgnoreCase(fileSuffix)) {
                return;
            }
        }
        log.error("文件头格式错误：{}",magicNumber);
        throw new RuntimeException( message);
    }

    //验证文件后缀
    private void doCheckSuffix(MultipartFile file, Set<String> suffixSet, String message) {
        String fileName = file.getOriginalFilename();
        String fileSuffix = FileUtil.extName(fileName);
        for (String suffix : suffixSet) {
            if (suffix.toUpperCase().equalsIgnoreCase(fileSuffix)) {
                return;
            }
        }
        log.error("文件后缀格式错误：{}", message);
        throw new RuntimeException( message);
    }

    //读取文件，获取文件头
    private String readMagicNumber(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] fileHeader = new byte[4];
            inputStream.read(fileHeader);
            return byteArray2Hex(fileHeader);
        } catch (IOException e) {
            log.error("文件读取错误：{}", e);
            throw new RuntimeException( "读取文件失败!");
        } finally {
            IOUtils.closeQuietly();
        }
    }

    private String byteArray2Hex(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        if (ArrayUtils.isEmpty(data)) {
            return null;
        }
        for (byte datum : data) {
            int v = datum & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        String result = stringBuilder.toString();
        return result;
    }
}