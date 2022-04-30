package com.terry.pigeon.vo.frontData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-16 13:39
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemoveGroupUserDo {
    private String groupId;

    private String[] userId;
}
