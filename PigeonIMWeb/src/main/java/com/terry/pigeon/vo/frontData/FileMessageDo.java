package com.terry.pigeon.vo.frontData;

import lombok.Data;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-29 14:24
 **/
@Data
public class FileMessageDo {
    private String channelType;

    private String receiverId;

    private String uploadId;
}
