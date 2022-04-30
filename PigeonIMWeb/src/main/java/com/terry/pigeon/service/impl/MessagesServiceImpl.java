package com.terry.pigeon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.enums.TalkEventEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.*;
import com.terry.pigeon.mapper.*;
import com.terry.pigeon.server.WebSocketChatServer;
import com.terry.pigeon.service.MessagesService;
import com.terry.pigeon.vo.MessageRecordsVo;
import com.terry.pigeon.vo.frontData.ClearMessage;
import com.terry.pigeon.vo.frontData.SendMessageTextDto;
import com.terry.pigeon.vo.message.TalkMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 消息表(Messages)表服务实现类
 *
 * @author makejava
 * @since 2022-03-03 21:46:58
 */
@Service("messagesService")
@Slf4j
public class MessagesServiceImpl extends ServiceImpl<MessagesMapper, Messages> implements MessagesService {


    @Resource
    private MessagesMapper messagesMapper;

    @Resource
    private SessionsMapper sessionsMapper;

    @Resource
    private FriendsMapper friendsMapper;

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private GroupUsersMapper groupsUserMapper;

    @Autowired
    private WebSocketChatServer webSocketChatServer;

    @Resource
    private MessageFileMapper messageFileMapper;

    /**
     * 获取消息记录
     * @param messageId
     * @param receiverUserId
     * @param channelType
     * @param limit
     * @return
     */
    @Override
    public ResponseResult getUserMessageRecords(String messageId, Long receiverUserId, Integer channelType, Integer limit) {


        List<Messages> messagesList = getMessageList(messageId,receiverUserId,channelType,limit);
        if (null == messagesList || messagesList.isEmpty()){
            return ResponseResult.successResult("没有聊天记录");
        }
        MessageRecordsVo messageRecordsVo = new MessageRecordsVo(messagesList,messagesList.get(0).getMessageId(),limit);

        return ResponseResult.successResult(messageRecordsVo);
    }

    /**
     * 消息发送实现方法，使用websocket推送消息并持久化到数据库
     * receiverId,channelType,text
     * @param sendMessageText
     * @return
     */
    @Override
    public ResponseResult sendMessageText(SendMessageTextDto sendMessageText) {


        Long sessionId = sessionsMapper.selectOne(new LambdaQueryWrapper<Sessions>()
                .select(Sessions::getSessionId)
                .eq(Sessions::getUserId, FriendsServiceImpl.getCurUser().getUserId())
                .eq(Sessions::getReceiverId, sendMessageText.getReceiverId())).getSessionId();
        Messages message = new Messages();
        message.setSessionId(sessionId);
        message.setUpdateAt(new Date());
        message.setFromUserId(FriendsServiceImpl.getCurUser()
                                                .getUserId());
        message.setChannelType(sendMessageText.getChannelType());
        message.setMsgText(sendMessageText.getText());
        message.setReceiverUserId(sendMessageText.getReceiverId());
        message.setCreatedAt(new Date());
        message.setMsgType(1);
        message.setIsRevoke(0);
        int insert = messagesMapper.insert(message);
        if (insert > 0){
            TalkMessage talkMessage = new TalkMessage(TalkEventEnum.EVENT_TALK.getEvent(),message);
            if (sendMessageText.getChannelType() == 1){
            message.setAvatar(FriendsServiceImpl.getCurUser().getAvatar());
            message.setNickName(FriendsServiceImpl.getCurUser().getNickName());
            webSocketChatServer.sendMessageToUser(message.getFromUserId().toString(),talkMessage);
            Users users = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUserId,message.getReceiverUserId()));
            message.setAvatar(users.getAvatar());
            message.setNickName(users.getNickName());
            webSocketChatServer.sendMessageToUser(message.getReceiverUserId().toString(),talkMessage);
            }else {
                Users users = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUserId, message.getFromUserId()));
                message.setAvatar(users.getAvatar());
                message.setNickName(users.getNickName());
                List<GroupUsers> userIdList = groupsUserMapper.selectList(new LambdaQueryWrapper<GroupUsers>().eq(GroupUsers::getGroupId,message.getReceiverUserId()).select(GroupUsers::getUserId));
                List<String> userIdLists = new ArrayList<>();
                for (GroupUsers groupUsers : userIdList) {
                    userIdLists.add(groupUsers.getUserId().toString());
                }
                webSocketChatServer.sendMessageToAll(userIdLists,talkMessage);
            }
            return ResponseResult.successResult();
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
        }
    }

    @Override
    public ResponseResult getRecordsHistory(String messageId, Long receiverUserId, Integer channelType, Integer limit,Integer msgType) {
        List<Messages> messagesList = getMessageList(messageId,receiverUserId,channelType,limit);

        if (msgType == 2){
            messagesList = messagesList.stream().filter(messages -> !Objects.isNull(messages.getFile())).collect(Collectors.toList());
        }
        MessageRecordsVo messageRecordsVo = new MessageRecordsVo(messagesList,messagesList.get(0).getMessageId(),limit);

        return ResponseResult.successResult(messageRecordsVo);
    }

    @Override
    public ResponseResult unreadMessageClear(ClearMessage clearMessage) {
        return ResponseResult.successResult();
    }

    @Override
    public ResponseResult deleteMessage(ClearMessage deleteMessage) {
        int i = 0;
        for (Integer messageId : deleteMessage.getMessageId()) {
           i = messagesMapper.deleteMessage(messageId);
        }


        return i > 0 ? ResponseResult.successResult() : ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
    }

    @Override
    public ResponseResult revokeMessage(Messages message) {


        int i = messagesMapper.revokeMessage(message.getMessageId());
        Messages messages = messagesMapper.selectOne(new LambdaQueryWrapper<Messages>().eq(Messages::getMessageId,message.getMessageId()));

        TalkMessage talkMessage = new TalkMessage(TalkEventEnum.EVENT_REVOKE_TALK.getEvent(), messages);
        if (messages.getChannelType() == 1){

            webSocketChatServer.sendMessageToUser(messages.getReceiverUserId().toString(),talkMessage);
            webSocketChatServer.sendMessageToUser(FriendsServiceImpl.getCurUser().getUserId().toString(),talkMessage);
        }else {
            List<GroupUsers> groupUsers = groupsUserMapper.selectList(new LambdaQueryWrapper<GroupUsers>()
                    .eq(GroupUsers::getGroupId, messages.getReceiverUserId())
                    .select(GroupUsers::getUserId));
            for (GroupUsers groupUser : groupUsers) {
                webSocketChatServer.sendMessageToUser(groupUser.getUserId().toString(),talkMessage);
            }
        }


        return i > 0 ? ResponseResult.successResult() : ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
    }


    /**
     * 获取聊天记录
     * @param messageId
     * @param receiverUserId
     * @param channelType
     * @param limit
     * @return
     */
    public List<Messages> getMessageList(String messageId, Long receiverUserId, Integer channelType, Integer limit){
        Page<Messages> page = new Page<>(1,limit);
        List<Messages> messagesList = new ArrayList<>();
        if (channelType == 2){
            messagesList = messagesMapper.selectPage(page,new LambdaQueryWrapper<Messages>()
                    .eq(Messages::getReceiverUserId,receiverUserId)
                    .orderByDesc(Messages::getCreatedAt)).getRecords();
        }else {

            Long sideSessionId = 0L;
            Long selfSessionId = 0L;
            Messages messages = new Messages();
            Sessions sessionId = sessionsMapper.selectOne(new LambdaQueryWrapper<Sessions>()
                    .select(Sessions::getSessionId)
                    .eq(Sessions::getUserId, receiverUserId)
                    .eq(Sessions::getReceiverId, FriendsServiceImpl.getCurUser()
                                                                   .getUserId()));
            if (!Objects.isNull(sessionId)) {
                sideSessionId = sessionId.getSessionId();
            }

            Sessions selfSession = sessionsMapper.selectOne(new LambdaQueryWrapper<Sessions>()
                    .select(Sessions::getSessionId)
                    .eq(Sessions::getUserId, FriendsServiceImpl.getCurUser()
                                                               .getUserId())
                    .eq(Sessions::getReceiverId, receiverUserId));
            if (!Objects.isNull(selfSession)) {
                selfSessionId = selfSession.getSessionId();
            }

            messagesList = messagesMapper.selectPage(page, new LambdaQueryWrapper<Messages>()
                                                 .in(Messages::getSessionId, new Long[]{selfSessionId, sideSessionId})
                                                 .orderByDesc(Messages::getCreatedAt))
                                         .getRecords();
        }
        if (messagesList.isEmpty()){
            return messagesList;
        }
        messagesList.stream().map(message -> {

            Users user = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUserId,message.getFromUserId()));
            message.setAvatar(user.getAvatar());
            message.setNickName(user.getNickName());
            if (message.getMsgType() == 2){
                MessageFile messageFile = messageFileMapper.selectOne(new LambdaQueryWrapper<MessageFile>().eq(MessageFile::getMessageId, message.getMessageId()));
                message.setFile(messageFile);
            }
            return message;
        }).collect(Collectors.toList());
        return messagesList;
    }
}
