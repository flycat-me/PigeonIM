package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-19 14:37
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionListVo {
    private Long sessionId;
    private int channelType;
    private Long receiverId;
    private Integer isTop;
    private Integer isDisturb;
    private Integer isOnline;
    private Integer isRobot;
    private String avatar;
    private String nickName;
    private String remarkName;
    private int unreadNum;
    private String msgText;
    private Date updateAt;
}
