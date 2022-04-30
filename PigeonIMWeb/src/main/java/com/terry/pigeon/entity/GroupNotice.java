package com.terry.pigeon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * (PigeonGroupNotice)表实体类
 *
 * @author makejava
 * @since 2022-03-16 15:35:26
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pigeon_group_notice")
public class GroupNotice implements Serializable {
    private static final long serialVersionUID = 493652911524612185L;
    //群公告主键    
    @TableId
    private Integer noticeId;

    //群组Id
    private Long groupId;
    //群公告标题
    private String noticeTitle;
    //群公告
    private String groupNotice;
    //创建时间
    private Date createTime;
    //0表示删除，1表示未删除
    private Integer delFlag;

    //创建者Id
    private Long createUserId;

    private Date updateAt;





}
