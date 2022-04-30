package com.terry.pigeon.server;

import com.alibaba.fastjson.JSON;
import com.terry.pigeon.entity.Message;
import com.terry.pigeon.mapper.FriendsMapper;
import com.terry.pigeon.utils.JwtUtil;
import com.terry.pigeon.vo.FriendListVo;
import com.terry.pigeon.vo.message.KeyboardMessage;
import com.terry.pigeon.vo.message.ReceiverMessage;
import com.terry.pigeon.vo.message.TalkMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-08 15:12
 **/
@Component
@ServerEndpoint("/chat/{userId}")
@Slf4j
public class WebSocketChatServer {

    private static FriendsMapper friendsMapper;

    @Resource
    public void setFriendsMapper(FriendsMapper friendsMapper){
        WebSocketChatServer.friendsMapper = friendsMapper;
    }


    /**
     * 全部在线会话  PS: 基于场景考虑 这里使用线程安全的Map存储会话对象。
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private String userId;

    /**
     * 当客户端打开连接：1.添加会话对象 2.更新在线人数
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String UserId, Session session) throws Exception {
        //onlineSessions.put(session.getId(), session);
        this.userId = getUserIdByToken(UserId);
        onlineSessions.put(userId, session);
        //连接成功返回信息
        WebsocketEvent.UserContentEvent(session);
        log.info("用户连接信息：{}",this.userId);

        //获取好友信息
        List<FriendListVo> friendList = friendsMapper.getFriendListByUserId(userId);
        for (FriendListVo friendListVo : friendList) {
            onlineSessions.forEach((id,curSession) -> {
                if (id.equals(friendListVo.getFriendUserId().toString())){
                    //调用对方的session发送数据，对方才能接收消息
                    WebsocketEvent.UserIsOnlineEvent(userId,curSession,1);
                }
            });
        }

    }

    /**
     * 当客户端发送消息：1.获取它的用户名和消息 2.发送消息给所有人
     * <p>
     * PS: 这里约定传递的消息为JSON字符串 方便传递更多参数！
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("-----接收到来自客户端信息----"+message);
        Message acceptMessage = JSON.parseObject(message, Message.class);
        if (acceptMessage.getEvent().equals("heartbeat")){
            Message message1 = new Message();
            message1.setEvent("heartbeat");
            message1.setContent("pong");
            session.getBasicRemote().sendText(JSON.toJSONString(message1));
        }else if(acceptMessage.getEvent().equals("event_keyboard")) {
            //获取对方的session,发送数据给对方
            ReceiverMessage receiverMessage = JSON.parseObject(message, ReceiverMessage.class);
            KeyboardMessage keyboardMessage = new KeyboardMessage(receiverMessage.getData().getSenderId(), receiverMessage.getData().getReceiverId());
            TalkMessage talkMessage = new TalkMessage("event_keyboard",keyboardMessage);
            sendMessageToUser(receiverMessage.getData().getReceiverId(),talkMessage);
        }
    }

    /**
     * 当关闭连接.移除会话对象 2.更新在线人数
     */
    @OnClose
    public void onClose(@PathParam("userId") String UserId,Session session) throws Exception {

        this.userId = getUserIdByToken(UserId);
        log.info("用户关闭连接{}",userId);
        //获取好友信息
        List<FriendListVo> friendList = friendsMapper.getFriendListByUserId(userId);
        for (FriendListVo friendListVo : friendList) {
            onlineSessions.forEach((id,curSession) -> {
                if (id.equals(friendListVo.getFriendUserId().toString())){
                    WebsocketEvent.UserIsOnlineEvent(userId,curSession,0);
                }
            });
        }
        onlineSessions.remove(getUserIdByToken(UserId));
    }

    /**
     * 当通信发生异常：打印错误日志
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 公共方法：发送信息给所有人
     */
    public void sendMessageToAll( List<String> userList,TalkMessage talkMessage) {
        onlineSessions.forEach((id, session) -> {

            for (String userId : userList) {
                if (userId.equals(id)){
                    try {
                        session.getBasicRemote().sendText(JSON.toJSONString(talkMessage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private static String getUserIdByToken(String token) throws Exception {
        return JwtUtil.parseJWT(token).getSubject();
    }
    public boolean sendMessageToUser(String fromUserId, TalkMessage talkMessage){

        onlineSessions.forEach((userId,session) ->{
            if (userId.equals(fromUserId)){
                try {
                    session.getBasicRemote().sendText(JSON.toJSONString(talkMessage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        });
        return false;
    }
    public boolean judgeUserIsOnline(String userId){
        List<String> onLineUser = new ArrayList<>();
        onlineSessions.forEach((id,session) -> {
            onLineUser.add(id);
        });
        for (int i = 0; i < onLineUser.size(); i++) {
            if (userId.equals(onLineUser.get(i))){
                return true;
            }
        }
        return false;
    }
}
