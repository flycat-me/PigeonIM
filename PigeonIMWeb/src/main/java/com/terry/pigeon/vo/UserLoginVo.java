package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-09 15:35
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginVo {
    private String access_token;
    private Integer expires_in;
}
