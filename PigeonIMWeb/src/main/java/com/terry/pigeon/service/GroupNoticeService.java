package com.terry.pigeon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.GroupNotice;


/**
 * (PigeonGroupNotice)表服务接口
 *
 * @author makejava
 * @since 2022-03-16 15:35:27
 */
public interface GroupNoticeService extends IService<GroupNotice> {

    //获取公告列表
    ResponseResult getGroupNoticeList(Long groupId);

    //添加公告
    ResponseResult editGroupNotice(GroupNotice groupNotice);
}
