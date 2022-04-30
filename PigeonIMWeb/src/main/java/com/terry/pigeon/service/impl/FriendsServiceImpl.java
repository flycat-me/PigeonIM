package com.terry.pigeon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terry.pigeon.common.constants.SystemConstants;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.enums.TalkEventEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.common.utils.BeanCopyUtils;
import com.terry.pigeon.entity.FriendRecords;
import com.terry.pigeon.entity.Friends;
import com.terry.pigeon.entity.Users;
import com.terry.pigeon.mapper.FriendRecordsMapper;
import com.terry.pigeon.mapper.FriendsMapper;
import com.terry.pigeon.mapper.UsersMapper;
import com.terry.pigeon.server.WebSocketChatServer;
import com.terry.pigeon.service.FriendsService;
import com.terry.pigeon.vo.FriendListVo;
import com.terry.pigeon.vo.FriendUserInfoVo;
import com.terry.pigeon.vo.frontData.ApplyFriendDo;
import com.terry.pigeon.vo.message.FriendMessage;
import com.terry.pigeon.vo.message.TalkMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 好友表(Friends)表服务实现类
 *
 * @author makejava
 * @since 2022-03-03 21:44:48
 */
@Service("friendsService")
@Slf4j
public class FriendsServiceImpl extends ServiceImpl<FriendsMapper, Friends> implements FriendsService {

    @Resource
    private FriendsMapper friendsMapper;

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private FriendRecordsMapper recordsMapper;

    @Autowired
    private WebSocketChatServer webSocketChatServer;


    @Override
    public ResponseResult getFriendList() {
        //通过工具方法获取当前登录的用户信息
        Users user = getCurUser();
        //通过当前登录用户的ID，在数据库中查询好友信息
        List<FriendListVo> friendList = friendsMapper.getFriendListByUserId(user.getUserId().toString());
        //返回好友列表信息
        return ResponseResult.successResult(friendList);
    }

    @Override
    public ResponseResult deleteFriendById(Friends friend) {
        //构造当前用户的好友查询条件，通过用户Id和好友的用户Id将删除字段设置为0（0表示删除）
        LambdaUpdateWrapper<Friends> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Friends::getUserId,getCurUser().getUserId());
        updateWrapper.eq(Friends::getFriendUserId,friend.getFriendUserId());
        updateWrapper.set(Friends::getDelFlag,SystemConstants.FRIEND_STATUS_DEL);
        //调用mybatis—plus内置的方法对数据库修改
        int update = friendsMapper.update(null,updateWrapper);
        if (update > 0){
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
        }else {
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS.getCode(),"删除好友失败");
        }

    }

    /**
     * 修改好友备注
     * @param friend
     * @return
     */
    @Override
    public ResponseResult editFriendRemark(Friends friend) {
        LambdaUpdateWrapper<Friends> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Friends::getFriendUserId,friend.getFriendUserId());
        updateWrapper.eq(Friends::getUserId,getCurUser().getUserId());
        updateWrapper.set(Friends::getFriendRemark,friend.getFriendRemark());
        int update = friendsMapper.update(null, updateWrapper);
        if (update > 0){
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
        }else {
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS.getCode(),"修改好友备注成功");
        }
    }

    /**
     * 查询好友个人信息
     * @param friend
     * @return
     */
    @Override
    public ResponseResult getFriendInfo(Friends friend) {
        Users friendUser = new Users();
        //通过好友的userId查询出个人信息
        friendUser = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUserId,friend.getFriendUserId()));
        /**
         * friend_records 无 friend 无  friendApply 0  friendStatus 1
         * friend_records 有 friend 无  friendApply 1  friendStatus 1
         * friend_records 无 friend 有  friendApply 2  friendStatus 2
         */
        //通过好友的用户Id和当前用户的userid查询用户之间是否为好友关系
        Friends associationFriend = friendsMapper.selectOne(new LambdaQueryWrapper<Friends>()
                .eq(Friends::getUserId,getCurUser().getUserId())
                .eq(Friends::getFriendUserId,friend.getFriendUserId()));
        // 查询是否发送申请
        FriendRecords friendRecords = recordsMapper.selectOne(new LambdaQueryWrapper<FriendRecords>()
                .eq(FriendRecords::getUserId,getCurUser().getUserId())
                .eq(FriendRecords::getFriendId,friend.getFriendUserId())
                .ne(FriendRecords::getStatus,SystemConstants.FRIEND_ACCEPT_STATUS));
        //不存在好友关系且未发送好友申请
        if (Objects.isNull(associationFriend) && Objects.isNull(friendRecords)){
            friendUser.setFriendStatus(1);
            friendUser.setFriendRemark("");
            friendUser.setFriendApply(0);
        }else if (!Objects.isNull(friendUser)){//已经是好友关系
            friendUser.setFriendStatus(2);
            friendUser.setFriendRemark(associationFriend.getFriendRemark());
            friendUser.setFriendApply(5);
        } else if (!Objects.isNull(friendRecords)){//不是好友关系，但已发送好友申请
            friendUser.setFriendStatus(5);
            friendUser.setFriendRemark("");
            friendUser.setFriendApply(1);
        }
        FriendUserInfoVo friendUserInfoVo = new FriendUserInfoVo();
        friendUserInfoVo = BeanCopyUtils.copyBean(friendUser,FriendUserInfoVo.class);
        return ResponseResult.successResult(friendUserInfoVo);
    }

    /**
     * 好友申请同意处理
     * @param friend （friendId，remark）
     * @return
     */
    @Override
    public ResponseResult friendAccept(Friends friend) {

        Friends acceptFriend = new Friends();
        acceptFriend.setFriendUserId(friend.getFriendUserId());
        acceptFriend.setFriendRemark(friend.getFriendRemark());
        acceptFriend.setCreatedAt(new Date());
        acceptFriend.setUserId(getCurUser().getUserId());
        int insert = friendsMapper.insert(acceptFriend);

        //将发起好友请求的好友关系建立

        int update = friendsMapper.updateFriendDelFlag(friend.getFriendUserId(),getCurUser().getUserId());


        //将好友请求表中表示请求状态的字段设置为1，表示已同意请求
        LambdaUpdateWrapper<FriendRecords> lambdaUpdateWrapper = new LambdaUpdateWrapper();
        lambdaUpdateWrapper.eq(FriendRecords::getUserId,acceptFriend.getFriendUserId());
        lambdaUpdateWrapper.eq(FriendRecords::getFriendId,acceptFriend.getUserId());
        lambdaUpdateWrapper.set(FriendRecords::getStatus,SystemConstants.FRIEND_ACCEPT_STATUS);
        recordsMapper.update(null,lambdaUpdateWrapper);



        if (insert > 0){
            return ResponseResult.successResult("添加好友成功");
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"添加好友失败，请稍后重试");
        }
    }

    /**
     * 添加好友,将发送方先添加到friend表中但将del_flag设置为0(表示好友关系还未建立)
     * 同时在好友申请表中添加好友申请记录
     */

    @Override
    public ResponseResult friendApply(ApplyFriendDo applyFriendDo) {

        //先生成好友申请记录
        FriendRecords friendRecords = new FriendRecords();
        friendRecords.setUserId(getCurUser().getUserId());//发送者Id
        friendRecords.setFriendId(applyFriendDo.getFriendUserId());//接收者id
        friendRecords.setStatus(0);
        friendRecords.setInformation(applyFriendDo.getInformation());
        friendRecords.setCreatedAt(new Date());

        recordsMapper.insert(friendRecords);

        //将消息通过websocket推送给用户
        //获取被添加好友的userId,使用其session将消息推送
        FriendMessage friendMessage = new FriendMessage(getCurUser().getUserId().toString()
                                                        ,applyFriendDo.getFriendUserId().toString()
                                                        ,applyFriendDo.getFriendRemark());
        TalkMessage talkMessage = new TalkMessage(TalkEventEnum.EVENT_FRIEND_APPLY.getEvent(), friendMessage);
        webSocketChatServer.sendMessageToUser(applyFriendDo.getFriendUserId().toString(),talkMessage);
        //将需要添加的好友添加到好友表
        Friends addFriend = new Friends();
        addFriend.setUserId(getCurUser().getUserId());
        addFriend.setFriendUserId(applyFriendDo.getFriendUserId());
        addFriend.setFriendRemark(applyFriendDo.getFriendRemark());
        addFriend.setFriendName(applyFriendDo.getFriendName());
        addFriend.setDelFlag(SystemConstants.FRIEND_STATUS_DEL);
        addFriend.setCreatedAt(new Date());

        int insert = friendsMapper.insert(addFriend);
        if (insert > 0) {
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
        }else {
            return ResponseResult.successResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"添加好友申请失败");
        }
    }

    public static Users getCurUser(){
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        Users user = (Users) authentication.getPrincipal();
        return user;
    }
}
