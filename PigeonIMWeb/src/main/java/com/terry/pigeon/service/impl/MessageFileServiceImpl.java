package com.terry.pigeon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terry.pigeon.entity.MessageFile;
import com.terry.pigeon.mapper.MessageFileMapper;
import com.terry.pigeon.service.MessageFileService;
import org.springframework.stereotype.Service;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-26 15:44
 **/
@Service
public class MessageFileServiceImpl extends ServiceImpl<MessageFileMapper, MessageFile> implements MessageFileService {
}
