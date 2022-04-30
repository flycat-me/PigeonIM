package com.terry.pigeon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.terry.pigeon.entity.Friends;
import com.terry.pigeon.vo.FriendListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 好友表(Friends)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-03 21:44:48
 */
public interface FriendsMapper extends BaseMapper<Friends> {

    public List<FriendListVo> getFriendListByUserId(String userId);

    public FriendListVo getFriendInfoByUserIdAndFriendId(@Param("userId") Long userId,@Param("friendId") Long friendId);

    int updateFriendDelFlag(@Param("friendUserId") Long friendUserId, @Param("userId") Long userId);
}
