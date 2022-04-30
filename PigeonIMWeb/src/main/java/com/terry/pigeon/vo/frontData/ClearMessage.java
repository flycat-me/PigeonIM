package com.terry.pigeon.vo.frontData;

import lombok.Data;

/**
 * @author terry_lang
 * @description
 * @since 2022-04-06 15:58
 **/
@Data
public class ClearMessage {
    private Integer channelType;

    private Long receiverId;

    private Integer[] messageId;
}
