package com.terry.pigeon.common.enums;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-27 22:00
 **/
public enum MessageFileType {


    VIDEO_TYPE(3),
    AUDIO_TYPE(2),
    FILE_TYPE(4),
    IMAGE_TYPE(1);
    Integer fileType;

    MessageFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getFileType() {
        return fileType;
    }
}
