package com.terry.pigeon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.GroupUsers;
import com.terry.pigeon.handler.SystemException;
import com.terry.pigeon.mapper.GroupUsersMapper;
import com.terry.pigeon.service.GroupUsersService;
import com.terry.pigeon.vo.frontData.AddUserGroupDo;
import com.terry.pigeon.vo.frontData.RemoveGroupUserDo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 群聊用户表(GroupUsers)表服务实现类
 *
 * @author makejava
 * @since 2022-03-03 21:48:01
 */
@Service("groupUsersService")
public class GroupUsersServiceImpl extends ServiceImpl<GroupUsersMapper, GroupUsers> implements GroupUsersService {

    @Resource
    GroupUsersMapper groupUsersMapper;

    @Override
    public ResponseResult setGroupDisturb(GroupUsers groupUser) {

        LambdaQueryWrapper<GroupUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupUsers::getGroupId,groupUser.getGroupId());
        queryWrapper.eq(GroupUsers::getUserId,FriendsServiceImpl.getCurUser().getUserId());
        GroupUsers groupUsers = new GroupUsers();
        groupUsers.setIsDisturb(groupUsers.getIsDisturb());
        int update = groupUsersMapper.update(groupUsers, queryWrapper);
        if (update > 0){
            return ResponseResult.successResult();
        }else {
            throw new SystemException(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
        }
    }

    /**
     * 删除群组用户
     * @param groupUser
     * @return
     * groupId，userId,
     */
    @Override
    public ResponseResult removeGroupMember(RemoveGroupUserDo groupUser) {
        Integer update = groupUsersMapper.removeGroupMembers(groupUser.getGroupId(),groupUser.getUserId());
        if (update > 0){
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
        }else {
            return ResponseResult.successResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
        }

    }

    /**
     * 修改用户群昵称接口
     * @param groupUsers
     * @return
     */
    @Override
    public ResponseResult editGroupNickName(GroupUsers groupUsers) {
        LambdaQueryWrapper<GroupUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupUsers::getGroupId,groupUsers.getGroupId());
        queryWrapper.eq(GroupUsers::getUserId,FriendsServiceImpl.getCurUser().getUserId());
        int update = groupUsersMapper.update(groupUsers, queryWrapper);
        if (update > 0){
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
    }

    /**
     * 用户退出群聊接口
     * @param groupUser
     * @return
     */
    @Override
    public ResponseResult groupMemberExit(GroupUsers groupUser) {
        String[] userId = {FriendsServiceImpl.getCurUser().getUserId().toString()};
        int update = groupUsersMapper.removeGroupMembers(groupUser.getGroupId().toString(),userId);
        if (update > 0){
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
    }

    @Override
    public ResponseResult addUserToGroup(AddUserGroupDo userGroupDo) {
        Integer integer = groupUsersMapper.addUserToGroup(userGroupDo);
        if (integer > 0 ){
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
    }
}
