package com.terry.pigeon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.terry.pigeon.entity.Groups;
import com.terry.pigeon.vo.GroupListVo;
import com.terry.pigeon.vo.GroupMemberVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 群聊表(Groups)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-03 21:47:34
 */
public interface GroupsMapper extends BaseMapper<Groups> {

    IPage<GroupListVo> selectGroupListPage(@Param("page") Page<GroupListVo> page, @Param("userId") Long userId);

    List<GroupMemberVo> getGroupMember(@Param("groupId") Long groupId);
}
