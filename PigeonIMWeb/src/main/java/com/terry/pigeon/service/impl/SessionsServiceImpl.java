package com.terry.pigeon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Groups;
import com.terry.pigeon.entity.MessageFile;
import com.terry.pigeon.entity.Messages;
import com.terry.pigeon.entity.Sessions;
import com.terry.pigeon.mapper.*;
import com.terry.pigeon.server.WebSocketChatServer;
import com.terry.pigeon.service.SessionsService;
import com.terry.pigeon.utils.RedisCache;
import com.terry.pigeon.vo.FriendListVo;
import com.terry.pigeon.vo.SessionListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 会话表(Sessions)表服务实现类
 *
 * @author makejava
 * @since 2022-03-03 21:46:19
 */
@Service("sessionsService")
public class SessionsServiceImpl extends ServiceImpl<SessionsMapper, Sessions> implements SessionsService {

    @Resource
    SessionsMapper sessionsMapper;

    @Resource
    UsersMapper usersMapper;

    @Resource
    FriendsMapper friendsMapper;

    @Resource
    GroupsMapper groupsMapper;

    @Resource
    MessagesMapper messagesMapper;

    @Autowired
    private WebSocketChatServer webSocketChatServer;

    @Autowired
    private RedisCache redisCache;

    @Resource
    private MessageFileMapper messageFileMapper;

    @Override
    public ResponseResult getSessionList() {

        List<Sessions> sessionDo = sessionsMapper.selectList(new LambdaQueryWrapper<Sessions>()
                .eq(Sessions::getUserId,FriendsServiceImpl.getCurUser().getUserId()));

        List<SessionListVo> sessionListVoList = new ArrayList<>();

        for (Sessions sessions : sessionDo) {
            //获取每个会话，最近的一条消息
            Messages messages = new Messages();
            LambdaQueryWrapper<Messages> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Messages::getSessionId,sessions.getSessionId());
            queryWrapper.orderByDesc(Messages::getUpdateAt);
            queryWrapper.last("limit 1");
            messages = messagesMapper.selectOne(queryWrapper);

            if (!Objects.isNull(messages)){
                if (messages.getMsgType() == 2){
                    MessageFile messageFile = messageFileMapper.selectOne(new LambdaQueryWrapper<MessageFile>()
                            .eq(MessageFile::getMessageId,messages.getMessageId()));
                    messages.setFile(messageFile);
                }
                sessions.setMessage(messages);
            }else {
                Messages nullMessage = new Messages();
                nullMessage.setUpdateAt(sessions.getCreatedAt());
                nullMessage.setSessionId(sessions.getSessionId());
                sessions.setMessage(nullMessage);
            }
            if (sessions.getChannelType() == 1){
                FriendListVo friendInfo = friendsMapper.getFriendInfoByUserIdAndFriendId(FriendsServiceImpl.getCurUser().getUserId(),sessions.getReceiverId());
//                sessionVo.setAvatar(friendInfo.getAvatar());
//                sessionVo.setNickName(friendInfo.getNickName());
//                sessionVo.setRemarkName(friendInfo.getFriendRemark());
                Integer isOnline = null;
                if (!Objects.isNull(redisCache.getCacheObject(friendInfo.getFriendUserId().toString()))){
                    isOnline = 1;
                }else {
                    isOnline = 0;
                }
                //System.out.println("-------------------->"+friendInfo.getFriendUserId()+"-----------"+webSocketChatServer.judgeUserIsOnline(friendInfo.getIsOnline().toString()));
                SessionListVo sessionVo = new SessionListVo(
                        sessions.getSessionId(), sessions.getChannelType(),
                        sessions.getReceiverId(),sessions.getIsTop(),
                        sessions.getIsDisturb(),isOnline,
                        0,friendInfo.getAvatar(),
                        friendInfo.getNickName(),friendInfo.getFriendRemark(),
                        sessions.getUnreadNum(),sessions.getMessage().getMsgText(),
                        sessions.getMessage().getUpdateAt());
                sessionListVoList.add(sessionVo);
            }else {
                Groups groups = groupsMapper.selectOne(new LambdaQueryWrapper<Groups>().eq(Groups::getGroupId, sessions.getReceiverId()));
                SessionListVo sessionVo = new SessionListVo(
                        sessions.getSessionId(), sessions.getChannelType(),
                        sessions.getReceiverId(),sessions.getIsTop(),
                        sessions.getIsDisturb(),0,
                        0,groups.getGroupAvatar(),
                        groups.getGroupName(),"",
                        sessions.getUnreadNum(),sessions.getMessage().getMsgText(),
                        sessions.getMessage().getUpdateAt());
                sessionListVoList.add(sessionVo);
            }
        }

        return ResponseResult.successResult(sessionListVoList);
    }

    /**
     * 创建会话
     * @return
     * @param session
     */
    @Override
    public ResponseResult createSession(Sessions session) {

        LambdaQueryWrapper<Sessions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Sessions::getReceiverId,session.getReceiverId());
        queryWrapper.eq(Sessions::getUserId,FriendsServiceImpl.getCurUser().getUserId());
        Sessions sessions = sessionsMapper.selectOne(queryWrapper);
        if (Objects.isNull(sessions)){
            Sessions addSession = new Sessions();
            addSession.setUserId(FriendsServiceImpl.getCurUser()
                                                   .getUserId());
            addSession.setReceiverId(session.getReceiverId());
            addSession.setChannelType(session.getChannelType());
            addSession.setIsTop(0);
            addSession.setCreatedAt(new Date());
            int insert = sessionsMapper.insert(addSession);

            //同时创建对方的会话
            LambdaQueryWrapper<Sessions> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Sessions::getUserId,session.getReceiverId());
            lambdaQueryWrapper.eq(Sessions::getReceiverId,FriendsServiceImpl.getCurUser().getUserId());
            Sessions sideSessions = sessionsMapper.selectOne(queryWrapper);
            if (Objects.isNull(sideSessions)){
                addSession.setUserId(session.getReceiverId());
                addSession.setReceiverId(FriendsServiceImpl.getCurUser().getUserId());
                sessionsMapper.insert(addSession);
            }

            return getSessionVo(session.getReceiverId());

            //return insert > 0 ? ResponseResult.successResult(AppHttpCodeEnum.SUCCESS) : ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
        }else {
            return getSessionVo(session.getReceiverId());
            }


    }

    @Override
    public ResponseResult setSessionTop(Sessions sessions) {
        sessionsMapper.updateById(sessions);
        return ResponseResult.successResult();
    }

    @Override
    public ResponseResult setSessionDisturb(Sessions sessions) {
        sessionsMapper.updateById(sessions);
        return ResponseResult.successResult();
    }

    //此处可能有问题
    @Override
    public ResponseResult deleteSessionById(Long sessionId) {
        int i = sessionsMapper.update(null,new LambdaUpdateWrapper<Sessions>().eq(Sessions::getSessionId,sessionId).set(Sessions::getDelFlag,0));
        return i > 0 ? ResponseResult.successResult() : ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
    }

    private ResponseResult getSessionVo(Long receiverId){
        SessionListVo sessionVo = sessionsMapper.getSessionByUserIdAndReceiveId(FriendsServiceImpl.getCurUser().getUserId(),receiverId);

        if (sessionVo.getChannelType() == 1){
            FriendListVo friendInfo = friendsMapper.getFriendInfoByUserIdAndFriendId(FriendsServiceImpl.getCurUser().getUserId(),receiverId);
            sessionVo.setAvatar(friendInfo.getAvatar());
            sessionVo.setNickName(friendInfo.getNickName());
            sessionVo.setRemarkName(friendInfo.getFriendRemark());
        }else {
            Groups groups = groupsMapper.selectOne(new LambdaQueryWrapper<Groups>().eq(Groups::getGroupId, sessionVo.getReceiverId()));
            sessionVo.setAvatar(groups.getGroupAvatar());
            sessionVo.setNickName(groups.getGroupName());
            sessionVo.setIsDisturb(groups.getIsDisturb());
        }
        return ResponseResult.successResult(sessionVo);
    }
}
