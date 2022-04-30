package com.terry.pigeon.common.constants;

/**
 * @author terry_lang
 * @description 字面值处理
 * @since 2022-02-28 15:22
 **/
public class SystemConstants {
    /**
     * 用户的状态表示
     */
    public static final int USER_STATUS_DEL = 1;

    /**
     * token 过期时间
     */
    public static final Integer USER_TOKEN_EXPIRES = 3600;

    /**
     * 0表示好友已删除
     */
    public static final int FRIEND_STATUS_DEL = 0;

    /**
     * 0 表示好友请求状态
     */
    public static final int FRIEND_APPLY_STATUS = 0;

    /**
     * 1 表示通过好友请求
     */
    public static final int FRIEND_ACCEPT_STATUS = 1;

    /**
     * 2 表示拒绝好友请求
     */
    public static final int FRIEND_REJECT_STATUS = 2;

    /**
     * 2 表示为群组创建者
     */
    public static final int IS_GROUP_LEADER = 2;

    public static final String OBS_PATH = "https://pigeon-im.obs.cn-east-3.myhuaweicloud.com:443/";
}
