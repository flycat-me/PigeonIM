package com.terry.pigeon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Sessions;


/**
 * 会话表(Sessions)表服务接口
 *
 * @author makejava
 * @since 2022-03-03 21:46:19
 */
public interface SessionsService extends IService<Sessions> {

    /**
     * 获取会话列表
     * @return
     */
    ResponseResult getSessionList();

    ResponseResult createSession(Sessions session);

    ResponseResult setSessionTop(Sessions sessions);

    ResponseResult setSessionDisturb(Sessions sessions);

    ResponseResult deleteSessionById(Long sessionId);
}
