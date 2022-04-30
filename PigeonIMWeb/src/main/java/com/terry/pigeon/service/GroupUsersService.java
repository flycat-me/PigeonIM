package com.terry.pigeon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.GroupUsers;
import com.terry.pigeon.vo.frontData.AddUserGroupDo;
import com.terry.pigeon.vo.frontData.RemoveGroupUserDo;


/**
 * 群聊用户表(GroupUsers)表服务接口
 *
 * @author makejava
 * @since 2022-03-03 21:48:01
 */
public interface GroupUsersService extends IService<GroupUsers> {

    /**
     * 设置用户群聊是否免打扰
     * @param groupUser
     * @return
     */
    ResponseResult setGroupDisturb(GroupUsers groupUser);

    ResponseResult removeGroupMember(RemoveGroupUserDo removeGroupUserDo);

    /**
     * 修改用户群昵称接口
     * @param groupUsers
     * @return
     */
    ResponseResult editGroupNickName(GroupUsers groupUsers);

    /**
     * 用户退出群聊接口
     * @param groupUser
     * @return
     */
    ResponseResult groupMemberExit(GroupUsers groupUser);

    /**
     * 邀请用户到群聊
     * @param userGroupDo
     * @return
     */
    ResponseResult addUserToGroup(AddUserGroupDo userGroupDo);
}
