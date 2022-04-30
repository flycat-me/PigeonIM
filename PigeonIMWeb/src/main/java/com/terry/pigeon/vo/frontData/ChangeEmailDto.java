package com.terry.pigeon.vo.frontData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-04-16 21:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeEmailDto {
    private String email;
    private String password;
}
