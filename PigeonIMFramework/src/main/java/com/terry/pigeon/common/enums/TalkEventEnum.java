package com.terry.pigeon.common.enums;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-23 16:06
 **/
public enum TalkEventEnum {
    /**
     * 对话消息通知 - 事件名
     */
    EVENT_TALK("event_talk","对话消息通知"),

    /**
     * 键盘输入事件通知 - 事件名
     */
    EVENT_KEYBOARD("event_keyboard","键盘输入事件通知"),
    /**
     * 用户在线状态通知 - 事件名
     */
    EVENT_ONLINE_STATUS("event_online_status","用户在线状态通知"),

    /**
     * 聊天消息撤销通知 - 事件名
     */
    EVENT_REVOKE_TALK("event_revoke_talk","聊天消息撤销通知"),
    /**
     * 好友申请消息通知 - 事件名
     */
    EVENT_FRIEND_APPLY("event_friend_apply","好友申请消息通知");

    String event;
    String msg;

    TalkEventEnum(String event, String msg) {
        this.event = event;
        this.msg = msg;
    }

    public String getEvent() {
        return event;
    }

    public String getMsg() {
        return msg;
    }
}
