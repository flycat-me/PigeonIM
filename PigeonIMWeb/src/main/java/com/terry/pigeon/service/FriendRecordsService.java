package com.terry.pigeon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.FriendRecords;
import com.terry.pigeon.entity.Friends;


/**
 * 好友申请表(FriendRecords)表服务接口
 *
 * @author makejava
 * @since 2022-03-03 21:48:48
 */
public interface FriendRecordsService extends IService<FriendRecords> {

    ResponseResult getFriendRecords(Integer pageNum, Integer pageSize);

    ResponseResult friendReject(Friends friends);

    ResponseResult friendUnread();
}
