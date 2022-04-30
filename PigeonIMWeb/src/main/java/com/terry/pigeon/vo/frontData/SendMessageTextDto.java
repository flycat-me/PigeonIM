package com.terry.pigeon.vo.frontData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-23 20:16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageTextDto {
    private Long receiverId;
    private Integer channelType;
    private String text;
}
