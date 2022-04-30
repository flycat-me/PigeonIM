package com.terry.pigeon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * 会话表(Sessions)表实体类
 *
 * @author makejava
 * @since 2022-03-03 21:46:19
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pigeonim_sessions")
public class Sessions implements Serializable {
    /**
     * 会话表
     */
    @TableId
    private Long sessionId;

    private Long userId;

    /**
     * 接收者Id
     */
    private Long receiverId;

    private Date createdAt;

    /**
     * 0.否 1.是
     */
    private Integer isTop;

    /**
     * 0.单聊 1.群聊
     */
    private Integer channelType;

    /**
     * 会话状态 0.正常 1.禁用
     */
    @TableLogic
    private Integer delFlag;

    private Integer unreadNum;

    private Integer isDisturb;

    @TableField(exist = false)
    private Messages message;

    private static final long serialVersionUID = 1L;



}
