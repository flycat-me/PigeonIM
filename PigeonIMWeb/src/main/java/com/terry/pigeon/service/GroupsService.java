package com.terry.pigeon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Groups;
import com.terry.pigeon.vo.frontData.CreateGroupDo;


/**
 * 群聊表(Groups)表服务接口
 *
 * @author makejava
 * @since 2022-03-03 21:47:34
 */
public interface GroupsService extends IService<Groups> {

    ResponseResult getGroupList(Integer pageNum,Integer pageSize);

    /**
     * 通过群组的id获取当前群组中的用户
     * @param groupId
     * @return
     */
    ResponseResult getGroupMember(Long groupId);

    ResponseResult getGroupInfo(Long groupId);

    /**
     *修改群组信息
     * @param group
     * @return
     */
    ResponseResult updateGroupInfo(Groups group);


    /**
     * 获取用户可邀请加入群组的好友列表
     * @param groupId
     * @return
     */
    ResponseResult getFriendMemberInvites(Long groupId);

    ResponseResult createGroup(CreateGroupDo createGroupDo);
}
