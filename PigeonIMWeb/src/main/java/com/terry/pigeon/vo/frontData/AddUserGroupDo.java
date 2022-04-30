package com.terry.pigeon.vo.frontData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-18 10:48
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserGroupDo {
    private Long groupId;
    private Long[] userIds;
}
