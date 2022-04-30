package com.terry.pigeon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.terry.pigeon.entity.GroupNotice;
import com.terry.pigeon.vo.GroupNoticeListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (PigeonGroupNotice)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-16 15:35:28
 */
public interface GroupNoticeMapper extends BaseMapper<GroupNotice> {

    List<GroupNoticeListVo> getGroupNoticeList(@Param("groupId") Long groupId);
}
