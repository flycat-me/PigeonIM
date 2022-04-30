package com.terry.pigeon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.GroupNotice;
import com.terry.pigeon.mapper.GroupNoticeMapper;
import com.terry.pigeon.service.GroupNoticeService;
import com.terry.pigeon.vo.GroupNoticeListVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (PigeonGroupNotice)表服务实现类
 *
 * @author makejava
 * @since 2022-03-16 15:35:28
 */
@Service("pigeonGroupNoticeService")
public class GroupNoticeServiceImpl extends ServiceImpl<GroupNoticeMapper, GroupNotice> implements GroupNoticeService {

    @Resource
    private GroupNoticeMapper noticeMapper;
    @Override
    public ResponseResult getGroupNoticeList(Long groupId) {

        List<GroupNoticeListVo> noticeList = noticeMapper.getGroupNoticeList(groupId);
        return ResponseResult.successResult(noticeList);
    }

    @Override
    public ResponseResult editGroupNotice(GroupNotice groupNotice) {

        Integer insert = null;
        if (groupNotice.getNoticeId() == 0){
            groupNotice.setCreateTime(new Date());
            groupNotice.setCreateUserId(FriendsServiceImpl.getCurUser().getUserId());
            insert = noticeMapper.insert(groupNotice);
        }else {
            groupNotice.setUpdateAt(new Date());
            insert = noticeMapper.updateById(groupNotice);
        }

        if (insert > 0){
            return ResponseResult.successResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_DATABASE_FAIL);
    }
}
