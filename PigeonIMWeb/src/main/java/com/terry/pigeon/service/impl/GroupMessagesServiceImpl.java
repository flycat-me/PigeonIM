package com.terry.pigeon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terry.pigeon.entity.GroupMessages;
import com.terry.pigeon.mapper.GroupMessagesMapper;
import com.terry.pigeon.service.GroupMessagesService;
import org.springframework.stereotype.Service;

/**
 * 群聊消息表(GroupMessages)表服务实现类
 *
 * @author makejava
 * @since 2022-03-03 21:48:25
 */
@Service("groupMessagesService")
public class GroupMessagesServiceImpl extends ServiceImpl<GroupMessagesMapper, GroupMessages> implements GroupMessagesService {

}
