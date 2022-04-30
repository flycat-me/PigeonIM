package com.terry.pigeon.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-26 21:11
 **/
@Slf4j
public class WebsocketEvent {

    //用户连接事件
    public static void UserContentEvent(Session session) {
        JSONObject message = new JSONObject();
        message.put("event", "connect");
        JSONObject content = new JSONObject();
        content.put("ping_interval",20);
        content.put("ping_timeout",20 * 3);
        message.put("content",content);
        try {
            session.getBasicRemote().sendText(JSONObject.toJSONString(message));
        } catch (IOException e) {
            log.error("Websocket发送消息失败");
            e.printStackTrace();
        }
    }
    //用户在线状态事件
    public static void UserIsOnlineEvent(String userId,Session session,Integer status){
        JSONObject message = new JSONObject();
        message.put("event","event_online_status");
        JSONObject content = new JSONObject();
        content.put("userId",userId);
        content.put("status",status);
        message.put("content",content);
        try {
            session.getBasicRemote().sendText(JSON.toJSONString(message));
        } catch (IOException e) {
            log.error("WebSocket发送用户在线事件失败");
            e.printStackTrace();
        }
    }
}
