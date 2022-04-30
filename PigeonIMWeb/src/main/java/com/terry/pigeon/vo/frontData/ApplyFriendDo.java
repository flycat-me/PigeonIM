package com.terry.pigeon.vo.frontData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-17 0:00
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyFriendDo {

    private Long friendUserId;

    private String information;

    private String friendRemark;

    private String friendName;
}
