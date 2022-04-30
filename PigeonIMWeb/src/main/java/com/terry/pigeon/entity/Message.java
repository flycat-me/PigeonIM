package com.terry.pigeon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebSocket 聊天消息类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String event;
    private String content;
    private String senderId;
    private String receiverId;

}