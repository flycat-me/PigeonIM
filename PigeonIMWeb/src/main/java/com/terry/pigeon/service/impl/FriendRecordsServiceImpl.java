package com.terry.pigeon.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terry.pigeon.common.constants.SystemConstants;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.FriendRecords;
import com.terry.pigeon.entity.Friends;
import com.terry.pigeon.entity.Users;
import com.terry.pigeon.mapper.FriendRecordsMapper;
import com.terry.pigeon.mapper.UsersMapper;
import com.terry.pigeon.service.FriendRecordsService;
import com.terry.pigeon.vo.PageNate;
import com.terry.pigeon.vo.PageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 好友申请表(FriendRecords)表服务实现类
 *
 * @author makejava
 * @since 2022-03-03 21:48:48
 */
@Service("friendRecordsService")
public class FriendRecordsServiceImpl extends ServiceImpl<FriendRecordsMapper, FriendRecords> implements FriendRecordsService {

    @Resource
    FriendRecordsMapper recordsMapper;

    @Resource
    UsersMapper usersMapper;
    /**
     * 好友申请记录查询
     * @param pageNum 每页大小
     * @param pageSize 页数
     * @return
     */
    @Override
    public ResponseResult getFriendRecords(Integer pageNum, Integer pageSize) {
        List<FriendRecords> recordsList = new ArrayList<>();

        //分页查询
        /**
         * userId(to) -> FriendId(from)
         * 查询好友申请，发送方将好友申请发送到被发送方，被发送方需要查询FriendId
         */
        Page<FriendRecords> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<FriendRecords> queryWrapper = new LambdaQueryWrapper<FriendRecords>()
                .eq(FriendRecords::getFriendId,FriendsServiceImpl.getCurUser().getUserId())
                .eq(FriendRecords::getStatus, SystemConstants.FRIEND_APPLY_STATUS);
        page = recordsMapper.selectPage(page,queryWrapper);

        recordsList = page.getRecords();

        recordsList.stream().map((friendRecords) -> {//查询申请方的信息
            Users user = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUserId,friendRecords.getUserId()));
            friendRecords.setAvatar(user.getAvatar());
            friendRecords.setNickName(user.getNickName());
            return friendRecords;
        }).collect(Collectors.toList());

        PageNate pageNate = new PageNate(page.getCurrent(),page.getSize(),page.getPages());
        PageVo pageVo = new PageVo(recordsList,pageNate);
        return ResponseResult.successResult(pageVo);
    }

    /**
     * 拒绝好友请求 friendUserId，rejectRemark
     * @param friend
     * @return
     */
    @Override
    public ResponseResult friendReject(Friends friend) {

        FriendRecords rejectFriend = new FriendRecords();
        rejectFriend.setStatus(SystemConstants.FRIEND_REJECT_STATUS);
        rejectFriend.setRejectRemark(friend.getFriendRemark());
        LambdaUpdateWrapper<FriendRecords> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FriendRecords::getFriendId,friend.getFriendUserId());
        //使用工具类从SpringSecurity中获取当前登录用户的Id
        lambdaUpdateWrapper.eq(FriendRecords::getUserId,FriendsServiceImpl.getCurUser().getUserId());
        int update = recordsMapper.update(rejectFriend, lambdaUpdateWrapper);
        if (update > 0){
           return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_NOT_FOUND,"操作失败");
        }

    }

    /**
     * 获取当前用户的未读好友记录
     * @return
     */
    @Override
    public ResponseResult friendUnread() {
        LambdaQueryWrapper<FriendRecords> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FriendRecords::getFriendId,FriendsServiceImpl.getCurUser().getUserId());
        queryWrapper.eq(FriendRecords::getStatus,SystemConstants.FRIEND_APPLY_STATUS);
        Integer unread_num = recordsMapper.selectCount(queryWrapper);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("unread_num",unread_num);
        return ResponseResult.successResult(jsonObject);
    }
}
