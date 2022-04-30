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
 * pigeonim_message_file
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pigeonim_message_file")
public class MessageFile implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer fileId;

    /**
     * 消息Id
     */
    private Integer messageId;

    /**
     * 上传的用户
     */
    private Long userId;

    /**
     * 来源
     */
    private Boolean source;

    /**
     * 类型
     */
    private Integer type;

    private Integer drive;

    /**
     * 文件原名
     */
    private String originalName;

    /**
     * 文件扩展名
     */
    private String suffix;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 文件的位置
     */
    private String url;

    /**
     * 上传时间
     */
    private Date createAt;

    private static final long serialVersionUID = 1L;
}