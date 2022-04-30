package com.terry.pigeon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * 群聊用户表(GroupUsers)表实体类
 *
 * @author makejava
 * @since 2022-03-03 21:48:01
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pigeonim_group_users")
public class GroupUsers implements Serializable {

    /**
     * 群组用户表主键
     */
    @TableId(type = IdType.AUTO)
    private Integer guId;

    /**
     * 群组ID
     */
    private Long groupId;

    /**
     * group中的用户ID
     */
    private Long userId;

    /**
     * 加入群聊时间
     */
    private Date joinAt;

    /**
     * 是否为群主，0 未知，1不是，2，是群主
     */
    private Integer isLeader;

    /**
     * 是否免打扰，0 不是，1是 默认为0
     */
    private Integer isDisturb;

    /**
     * 退出群聊时间
     */
    private Date quitAt;

    /**
     * 退出标志，0退出，1未退出
     */
    private Integer delFlag;

    /**
     * 群组中昵称
     */
    private String groupNickname;

    private static final long serialVersionUID = 1L;



}
