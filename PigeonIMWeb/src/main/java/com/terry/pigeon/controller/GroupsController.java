package com.terry.pigeon.controller;


import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.GroupNotice;
import com.terry.pigeon.entity.GroupUsers;
import com.terry.pigeon.entity.Groups;
import com.terry.pigeon.service.GroupNoticeService;
import com.terry.pigeon.service.GroupUsersService;
import com.terry.pigeon.service.GroupsService;
import com.terry.pigeon.vo.frontData.AddUserGroupDo;
import com.terry.pigeon.vo.frontData.CreateGroupDo;
import com.terry.pigeon.vo.frontData.RemoveGroupUserDo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 群聊表(Groups)表控制层
 *
 * @author makejava
 * @since 2022-03-03 21:47:34
 */
@RestController
@RequestMapping("/api/group")
public class GroupsController {
    /**
     * 服务对象
     */
    @Resource
    private GroupsService groupsService;

    @Resource
    private GroupUsersService groupUsersService;

    @Resource
    private GroupNoticeService  noticeService;

    /**
     * 通过当前登录用户查询groupList
     * @return
     */
    @GetMapping(value = "/list")
    public ResponseResult getGroupList(Integer pageNum,Integer pageSize){
        return groupsService.getGroupList(pageNum,pageSize);
    }

    @GetMapping(value = "/listGroup")
    public ResponseResult getGroupList(){
        return groupsService.getGroupList(1,50);
    }

    /**
     * 创建group
     */
    @PostMapping(value = "/create")
    public ResponseResult createGroup(@RequestBody CreateGroupDo createGroupDo){
        return groupsService.createGroup(createGroupDo);
    }

    /**
     * 获取群组中用户
     */
    @GetMapping(value = "/member/list")
    public ResponseResult getGroupMember(Long groupId){
        return groupsService.getGroupMember(groupId);
    }

    /**
     * 获取群组信息
     */
    @GetMapping(value = "/info")
    public ResponseResult getCurGroupInfo(Long groupId){
        return groupsService.getGroupInfo(groupId);
    }

    /**
     * 设置当前用户的群聊为免打扰
     * @param groupUser @param groupId 群Id
     * *                  @param isDisturb 状态
     */
    @PostMapping(value = "/disturb")
    public ResponseResult setGroupDisturb(@RequestBody GroupUsers groupUser){
        return groupUsersService.setGroupDisturb(groupUser);
    }

    /**
     * 修改群组信息
     * @param group
     * GroupId,groupName,avatar,groupInfo
     * @return
     */
    @PostMapping(value = "/setting")
    public ResponseResult updateGroupInfo(@RequestBody Groups group){
        return groupsService.updateGroupInfo(group);
    }

    /**
     * 移除群组用户
     * @param removeGroupUserDo
     * @return
     */
    @PostMapping(value = "/member/remove")
    public ResponseResult groupMemberRemove(@RequestBody RemoveGroupUserDo removeGroupUserDo){
        return groupUsersService.removeGroupMember(removeGroupUserDo);
    }

    /**
     * 获取群聊群公告列表
     * @param groupId
     * @return
     */
    @GetMapping(value = "/notice/list")
    public ResponseResult getGroupNoticeList(Long groupId){
        return noticeService.getGroupNoticeList(groupId);
    }

    /**
     * 添加修改群聊公告接口
     * @param groupNotice
     * @return
     */
    @PostMapping(value = "/notice/edit")
    public ResponseResult editGroupNotice(@RequestBody GroupNotice groupNotice){
        return noticeService.editGroupNotice(groupNotice);
    }

    /**
     * 修改用户群昵称接口
     * @param groupUser
     * @return
     */
    @PostMapping(value = "/member/editGroupNickName")
    public ResponseResult editGroupNickName(@RequestBody GroupUsers groupUser){
        return groupUsersService.editGroupNickName(groupUser);
    }

    /**
     * 用户退出群聊接口
     * @param groupUser
     * @return
     */
    @PostMapping(value = "/member/exit")
    public ResponseResult groupMemberExit(@RequestBody GroupUsers groupUser){
        return groupUsersService.groupMemberExit(groupUser);
    }

    /**
     * 获取用户可邀请加入群组的好友列表
     */
    @GetMapping(value = "/member/invites")
    public ResponseResult getFriendMemberInvites(Long groupId){
        return groupsService.getFriendMemberInvites(groupId);
    }


    /**
     * 将邀请的用户添加到对应的群组中
     */
    @PostMapping(value = "/invite")
    public ResponseResult addUserToGroup(@RequestBody AddUserGroupDo userGroupDo){
        return groupUsersService.addUserToGroup(userGroupDo);
    }

}

