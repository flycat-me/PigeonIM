package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-16 17:04
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupNoticeListVo {
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

    private String avatar;

    private String nickName;
}
