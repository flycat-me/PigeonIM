package com.terry.pigeon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
/**
 * 用户表(Users)表实体类
 *
 * @author makejava
 * @since 2022-03-02 17:18:45
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pigeonim_users")
@ToString
public class Users implements Serializable {
    //用户ID
    @TableId("user_id")
    private Long userId;

    //用户账号
    @NotNull(message = "用户手机号不能为空")
    @Pattern(regexp = "^1[3|4|5|6|7|8|9][0-9]{9}$", message = "用户手机号不合法")
    private String mobile;
    //用户昵称
    private String nickName;
    //邮箱
    private String email;
    //密码
    @NotNull(message = "密码不能为空")
    private String password;
    //创建时间
    private Date createdAt;
    //更新时间
    private Date updatedAt;
    //头像
    private String avatar;
    
    private Date deletedAt;
    //0 离线 1 在线
    private Integer status;
    //账号状态（0正常 1停用）
    private String userStatus;
    //用户简介
    private String motto;
    //0 未知 1.男 2.女
    private Integer gender;
    
    private Integer age;
    //最后登录时间
    private Date lastLoginTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;

    @TableField(exist = false)
    private Integer friendStatus;
    @TableField(exist = false)
    private String friendRemark;
    @TableField(exist = false)
    private Integer friendApply;



}
