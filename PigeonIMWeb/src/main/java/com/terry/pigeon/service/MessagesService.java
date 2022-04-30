package com.terry.pigeon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Messages;
import com.terry.pigeon.vo.frontData.ClearMessage;
import com.terry.pigeon.vo.frontData.SendMessageTextDto;


/**
 * 消息表(Messages)表服务接口
 *
 * @author makejava
 * @since 2022-03-03 21:46:58
 */
public interface MessagesService extends IService<Messages> {

    ResponseResult getUserMessageRecords(String messageId, Long receiverUserId, Integer channelType, Integer limit);

    /**
     * 发送文本消息
     * @param sendMessageText
     * @return
     */
    ResponseResult sendMessageText(SendMessageTextDto sendMessageText);

    ResponseResult getRecordsHistory(String messageId, Long receiverUserId, Integer channelType, Integer limit,Integer msgType);

    ResponseResult unreadMessageClear(ClearMessage clearMessage);

    ResponseResult deleteMessage(ClearMessage deleteMessage);

    ResponseResult revokeMessage(Messages message);
}
