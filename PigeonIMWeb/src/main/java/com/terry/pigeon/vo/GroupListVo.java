package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-14 15:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupListVo {

    private Long userId;

    private Long groupId;

    private String groupName;

    private String groupInfo;

    private String groupAvatar;

    private Integer isLeader;

    private Integer isDisturb;
}
