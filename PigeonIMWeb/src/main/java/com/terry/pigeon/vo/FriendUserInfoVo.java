package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-13 12:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendUserInfoVo {
    private Long userId;

    private String mobile;

    private String motto;

    private String avatar;

    private String nickName;

    private Integer gender;

    private String email;

    private Integer friendStatus;

    private String friendRemark;

    private Integer friendApply;
}
