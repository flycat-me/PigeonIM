package com.terry.pigeon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * 消息表(Messages)表实体类
 *
 * @author makejava
 * @since 2022-03-03 21:46:58
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pigeonim_messages")
public class Messages implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer messageId;

    /**
     * 会话Id
     */
    private Long sessionId;

    private String msgText;

    private Date createdAt;

    /**
     * 发送方ID
     */
    private Long fromUserId;

    /**
     * 接收方ID
     */
    private Long receiverUserId;

    /**
     * 0 未读 1已读
     */
    private Integer isRead;

    private Integer msgType;

    /**
     * 1.好友 2.群聊
     */
    private Integer channelType;

    private Integer delFlag;

    private Integer isRobot;

    private Date updateAt;

    private Integer isRevoke;

    private Integer isMark;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private MessageFile file;

    private static final long serialVersionUID = 1L;



}
