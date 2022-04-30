package com.terry.pigeon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.terry.pigeon.entity.Messages;
import org.apache.ibatis.annotations.Param;


/**
 * 消息表(Messages)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-03 21:46:58
 */
public interface MessagesMapper extends BaseMapper<Messages> {

    /**
     * 获取消息表中最新的消息
     * @param sessionId
     */
    Messages recentMessageGet(@Param("sessionId") Long sessionId);

    int deleteMessage(@Param("messageId") Integer messageId);

    int revokeMessage(@Param("messageId") Integer messageId);
}
