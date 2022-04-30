package com.terry.pigeon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terry.pigeon.common.constants.SystemConstants;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.GroupNotice;
import com.terry.pigeon.entity.GroupUsers;
import com.terry.pigeon.entity.Groups;
import com.terry.pigeon.entity.Users;
import com.terry.pigeon.handler.SystemException;
import com.terry.pigeon.mapper.*;
import com.terry.pigeon.service.GroupsService;
import com.terry.pigeon.vo.*;
import com.terry.pigeon.vo.frontData.AddUserGroupDo;
import com.terry.pigeon.vo.frontData.CreateGroupDo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 群聊表(Groups)表服务实现类
 *
 * @author makejava
 * @since 2022-03-03 21:47:34
 */
@Service("groupsService")
public class GroupsServiceImpl extends ServiceImpl<GroupsMapper, Groups> implements GroupsService {

    @Resource
    GroupsMapper groupsMapper;

    @Resource
    UsersMapper usersMapper;

    @Resource
    GroupUsersMapper groupUsersMapper;

    @Resource
    GroupNoticeMapper noticeMapper;

    @Resource
    FriendsMapper friendsMapper;

    @Override
    public ResponseResult getGroupList(Integer pageNum, Integer pageSize) {
        Users curUser = FriendsServiceImpl.getCurUser();

        Page<GroupListVo> page = new Page<>(pageNum,pageSize);

        LambdaQueryWrapper<GroupListVo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupListVo::getUserId,curUser.getUserId());

        IPage<GroupListVo> iPage = groupsMapper.selectGroupListPage(page, curUser.getUserId());

        List<GroupListVo> groupsList = iPage.getRecords();

        PageNate pageNate = new PageNate(page.getCurrent(),page.getSize(),page.getPages());
        PageVo pageVo = new PageVo(groupsList,pageNate);
        return ResponseResult.successResult(pageVo);
    }

    @Override
    public ResponseResult getGroupMember(Long groupId) {

        List<GroupMemberVo> groupMemberList = new ArrayList();
        groupMemberList = groupsMapper.getGroupMember(groupId);
        return ResponseResult.successResult(groupMemberList);
    }

    /**
     * 根据群组Id获取群组信息
     * @param groupId
     * @return
     */
    @Override
    public ResponseResult getGroupInfo(Long groupId) {

        LambdaQueryWrapper<Groups> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Groups::getGroupId,groupId);
        Groups group = groupsMapper.selectOne(queryWrapper);

        LambdaQueryWrapper<GroupUsers> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupUsers::getGroupId,groupId);
        wrapper.eq(GroupUsers::getUserId,FriendsServiceImpl.getCurUser().getUserId());

        GroupUsers groupUsers = groupUsersMapper.selectOne(wrapper);

        group.setGroupNickName(groupUsers.getGroupNickname());
        group.setIsDisturb(groupUsers.getIsDisturb());
        Long curUserId = FriendsServiceImpl.getCurUser().getUserId();
        if (curUserId.equals(group.getCreateUserId())) {
            group.setIsManager(true);
        }else {
            group.setIsManager(false);
        }
        List<GroupNotice> noticeList = noticeMapper.selectList(new LambdaQueryWrapper<GroupNotice>().eq(GroupNotice::getGroupId,groupId));

        group.setGroupNotice(noticeList);

        return ResponseResult.successResult(group);
    }


    /**
     * 修改群组信息
     * @param group
     * @return
     * GroupId,groupName,avatar,groupInfo
     */
    @Override
    public ResponseResult updateGroupInfo(Groups group) {

        LambdaQueryWrapper<Groups> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Groups::getGroupId,group.getGroupId());
        int update = groupsMapper.update(group, queryWrapper);
        if (update > 0){
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
        }else {
            return ResponseResult.successResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
        }
    }

    @Override
    public ResponseResult getFriendMemberInvites(Long groupId) {
        List<FriendListVo> listUsers = friendsMapper.
                getFriendListByUserId(FriendsServiceImpl.getCurUser().getUserId().toString());
        List<GroupUsers> groupUsersList = groupUsersMapper.selectList(new LambdaQueryWrapper<GroupUsers>()
                .eq(GroupUsers::getGroupId, groupId));
        //获取当前登录用户的所有好友，然后去除已经在当前群组的好友
        for (FriendListVo listUser : listUsers) {
            for (GroupUsers groupUsers : groupUsersList) {
                if (listUser.getFriendUserId().equals(groupUsers.getUserId())){
                    listUsers.remove(listUser);
                }
            }
        }
        return ResponseResult.successResult(listUsers);
    }

    /**
     * 创建群组
     * @param createGroupDo
     * @return
     */
    @Override
    public ResponseResult createGroup(CreateGroupDo createGroupDo) {

        //获取当前用户信息
        Users user = FriendsServiceImpl.getCurUser();

        Groups group = new Groups();
        group.setGroupName(createGroupDo.getGroupName());
        group.setCreateUserId(user.getUserId());
        group.setManagerNickname(user.getNickName());
        group.setGroupInfo(createGroupDo.getGroupInfo());
        group.setGroupAvatar(createGroupDo.getGroupAvatar());
        group.setCreatedAt(new Date());
        //创建群组
        int insert = groupsMapper.insert(group);
        if ( insert > 0 ){
            GroupUsers groupUser = new GroupUsers();
            groupUser.setGroupId(group.getGroupId());
            groupUser.setIsLeader(SystemConstants.IS_GROUP_LEADER);
            groupUser.setUserId(user.getUserId());
            groupUser.setJoinAt(new Date());
            //添加创建者到群组中
            int insert1 = groupUsersMapper.insert(groupUser);
            if (insert1 > 0){
                AddUserGroupDo addUserGroupDo = new AddUserGroupDo(group.getGroupId(),createGroupDo.getUserIds());
                //添加其他用户到群组中
                groupUsersMapper.addUserToGroup(addUserGroupDo);
                return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
            }else {
                return ResponseResult.errorResult(AppHttpCodeEnum.CREATE_GROUP_FAIL);
            }
        }else {
           throw new SystemException(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
        }


    }
}
