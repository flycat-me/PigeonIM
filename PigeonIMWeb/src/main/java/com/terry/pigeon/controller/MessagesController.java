package com.terry.pigeon.controller;




import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.terry.pigeon.service.MessagesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 消息表(Messages)表控制层
 *
 * @author makejava
 * @since 2022-03-03 21:46:58
 */
@RestController
@RequestMapping("messages")
public class MessagesController {
    /**
     * 服务对象
     */
    @Resource
    private MessagesService messagesService;
}

