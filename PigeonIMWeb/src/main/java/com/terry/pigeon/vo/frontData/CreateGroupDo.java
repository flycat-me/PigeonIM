package com.terry.pigeon.vo.frontData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-18 18:34
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateGroupDo {

    private String groupAvatar;

    private String groupName;

    private String groupInfo;

    private Long[] userIds;
}
