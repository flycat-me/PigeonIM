package com.terry.pigeon.annotation;

import com.terry.pigeon.common.enums.FileType;

import java.lang.annotation.*;

/**
 * @author terry_lang
 * @description 自定义注解用于检测上传文件
 * @since 2022-03-26 16:42
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UploadFileCheck {
    /**
     * 校验不通过提示信息
     *
     * @return
     */
    String message() default "不支持的文件格式";

    /**
     * 校验方式
     */
    CheckType type() default CheckType.SUFFIX;

    /**
     * 支持的文件后缀
     *
     * @return
     */
    String[] supportedSuffixes() default {};

    /**
     * 支持的文件类型
     *
     * @return
     */
    FileType[] supportedFileTypes() default {};

    enum CheckType {
        /**
         * 仅校验后缀
         */
        SUFFIX,
        /**
         * 校验文件头(魔数)
         */
        MAGIC_NUMBER,
        /**
         * 同时校验后缀和文件头
         */
        SUFFIX_MAGIC_NUMBER
    }
}
