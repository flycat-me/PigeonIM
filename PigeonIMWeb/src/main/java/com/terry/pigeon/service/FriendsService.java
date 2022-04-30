package com.terry.pigeon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Friends;
import com.terry.pigeon.vo.frontData.ApplyFriendDo;


/**
 * 好友表(Friends)表服务接口
 *
 * @author makejava
 * @since 2022-03-03 21:44:48
 */
public interface FriendsService extends IService<Friends> {

    ResponseResult getFriendList();

    ResponseResult deleteFriendById(Friends friendId);

    ResponseResult editFriendRemark(Friends friend);

    ResponseResult getFriendInfo(Friends friend);

    ResponseResult friendAccept(Friends friend);

    ResponseResult friendApply(ApplyFriendDo applyFriendDo);
}
