package com.terry.pigeon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.terry.pigeon.entity.Sessions;
import com.terry.pigeon.vo.SessionListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 会话表(Sessions)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-03 21:46:19
 */
public interface SessionsMapper extends BaseMapper<Sessions> {

    List<SessionListVo> getSessionListByUserId(@Param("userId") Long userId);
    List<Sessions> getSessionList(@Param("userId") Long userId);
    SessionListVo getSessionByUserIdAndReceiveId(@Param("userId") Long userId, @Param("receiverId") Long receiverId);
}
