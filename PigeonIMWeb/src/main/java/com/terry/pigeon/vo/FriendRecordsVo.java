package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-13 15:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRecordsVo {
    private int recordsId;

    private int userId;

    private int friendId;

    private String information;

    private String nickName;

    private String avatar;

    private String createdAt;
}
