package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-14 17:54
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupMemberVo {

    private Integer guId;

    private Long userId;

    private String avatar;

    private String nickName;

    private String groupNickName;

    private String motto;

    private Integer isLeader;

    private Integer gender;
}
