package com.terry.pigeon.controller;

import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Friends;
import com.terry.pigeon.entity.Users;
import com.terry.pigeon.service.FriendRecordsService;
import com.terry.pigeon.service.FriendsService;
import com.terry.pigeon.service.UsersService;
import com.terry.pigeon.vo.frontData.ApplyFriendDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author terry_lang
 * @description 好友相关接口
 * @since 2022-03-13 0:07
 **/
@Slf4j
@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Resource
    private FriendsService friendsService;

    @Resource
    FriendRecordsService friendRecordsService;

    @Resource
    UsersService usersService;

    /**
     * 好友列表获取
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getFriendList(){
        return friendsService.getFriendList();
    }

    /**
     * 好友删除接口
     * @param friend 具体参数为 (friendUserId) 需要删除的好友用户Id
     * @return
     */
    @PostMapping("/delete")
    public ResponseResult deleteFriendById(@RequestBody Friends friend){
        return friendsService.deleteFriendById(friend);
    }

    /**
     * 好友备注修改接口
     * @param friend
     * @return
     */
    @PostMapping("/edit-remark")
    public ResponseResult editFriendRemark(@RequestBody Friends friend){
        return friendsService.editFriendRemark(friend);
    }

    /**
     * 好友信息查询接口
     */
    @PostMapping("/friendUserInfo")
    public ResponseResult getFriendInfo(@RequestBody Friends friend){
        return friendsService.getFriendInfo(friend);
    }

    /**
     * 好友添加接口
     * {
     *     "friendId": "1502910010080919554", 需要添加的用户id
     *     "information": "我们好像在哪儿见过" 添加的信息
     *     "friendRemark" 好友备注
     * }
     */
    @PostMapping("/apply/create")
    public ResponseResult friendCreate(@RequestBody ApplyFriendDo applyFriendDo){

        return friendsService.friendApply(applyFriendDo);
    }

    /**
     * 好友搜索接口
     * @param user mobile
     */
    @PostMapping(value = "/searchFriend")
    public ResponseResult searchFriend(@RequestBody Users user){
        return usersService.searchFriend(user);
    }

    /**
     * 好友申请查询接口
     */
    @GetMapping(value = "/apply/getFriendRecords")
    public ResponseResult getFriendRecords(Integer pageNum,Integer pageSize){
        return friendRecordsService.getFriendRecords(pageNum,pageSize);
    }

    /**
     *
     * @param friend 同意好友请求  FriendUserId 好友的用户Id，FriendRemark好友备注
     * @return
     */
    @PostMapping(value = "/apply/accept")
    public ResponseResult friendAccept(@RequestBody Friends friend){
        return friendsService.friendAccept(friend);
    }

    /**
     * 拒绝好友申请 FriendUserId rejectRemark
     * @param friend
     * @return
     */
    @PostMapping("/apply/reject")
    public ResponseResult friendReject(@RequestBody Friends friend){

        return friendRecordsService.friendReject(friend);
    }

    @GetMapping("/apply/unread-num")
    public ResponseResult friendUnread(){
        return friendRecordsService.friendUnread();
    }


}
