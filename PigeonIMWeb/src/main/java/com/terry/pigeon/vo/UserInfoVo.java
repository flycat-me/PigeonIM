package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-12 12:33
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoVo {
    private long userId;

    private String mobile;

    private String nickName;

    private String avatar;

    private int gender;

    //private String password;

    private String motto;

    private String email;

//    @TableField(exist = false)
//    private int is_robot;

    private Date createdAt;

    private Date updatedAt;
}
