package com.terry.pigeon.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 群聊消息表(GroupMessages)表实体类
 *
 * @author makejava
 * @since 2022-03-03 21:48:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pigeonim_group_messages")
public class GroupMessages implements Serializable {
    private static final long serialVersionUID = 679289622963606347L;
        
    @TableId
    private Integer gmId;

    //消息类型 1.文本消息 2.图文消息 3.语音消息
    private Integer msgType;
    //消息内容
    private String msg;
    //群聊id
    private Integer groupId;
    //消息发送人
    private Integer fromId;
    
    private Date createdAt;



}
