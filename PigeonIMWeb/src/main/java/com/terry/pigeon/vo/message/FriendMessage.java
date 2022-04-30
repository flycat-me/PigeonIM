package com.terry.pigeon.vo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-27 17:26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendMessage {
    private String senderId;
    private String receiverId;
    private String remark;
}
