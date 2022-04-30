package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-13 0:13
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendListVo {

    private Long friendUserId;

    private String nickName;

    private Integer gender;

    private String motto;

    private String avatar;

    private String friendRemark;

    private Integer isOnline;
}
