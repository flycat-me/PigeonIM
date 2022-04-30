package com.terry.pigeon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 群聊表(Groups)表实体类
 *
 * @author makejava
 * @since 2022-03-03 21:47:34
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pigeonim_groups")
public class Groups implements Serializable {
    /**
     * 群组的ID
     */
    @TableId
    private Long groupId;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 群组名称
     */
    private String groupName;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 群组简介
     */
    private String groupInfo;

    /**
     * 群组头像
     */
    private String groupAvatar;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 群组公告
     */

    /**
     * 删除标志，0表示删除 ，1表示未删除
     */
    private Integer delFlag;

    @TableField(exist = false)
    private Boolean isManager;

    private String managerNickname;

    @TableField(exist = false)
    private String groupNickName;

    @TableField(exist = false)
    private Integer isDisturb;

    @TableField(exist = false)
    private List<GroupNotice> groupNotice;

    private static final long serialVersionUID = 1L;


}
