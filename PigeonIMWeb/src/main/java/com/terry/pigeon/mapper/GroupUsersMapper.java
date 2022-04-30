package com.terry.pigeon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.terry.pigeon.entity.GroupUsers;
import com.terry.pigeon.vo.frontData.AddUserGroupDo;
import org.apache.ibatis.annotations.Param;


/**
 * 群聊用户表(GroupUsers)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-03 21:48:01
 */
public interface GroupUsersMapper extends BaseMapper<GroupUsers> {

    Integer removeGroupMembers(@Param("groupId") String groupId, @Param("userIds") String[] userId);

    int groupMemberExit(@Param("groupUser") GroupUsers groupUser);

    Integer addUserToGroup(@Param("userGroupDo") AddUserGroupDo userGroupDo);
}
