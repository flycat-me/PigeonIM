package com.terry.pigeon.controller;

import com.terry.pigeon.annotation.UploadFileCheck;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.Messages;
import com.terry.pigeon.entity.Sessions;
import com.terry.pigeon.service.MessagesService;
import com.terry.pigeon.service.ObsUploadService;
import com.terry.pigeon.service.SessionsService;
import com.terry.pigeon.vo.frontData.ClearMessage;
import com.terry.pigeon.vo.frontData.FileMessageDo;
import com.terry.pigeon.vo.frontData.SendMessageTextDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Resource
    private SessionsService sessionsService;

    @Resource
    private MessagesService messagesService;

    @Resource
    private ObsUploadService uploadService;


    @GetMapping(value = "/list")
    public ResponseResult getSessionList(){
        return sessionsService.getSessionList();
    }

    /**
     * 创建会话
     * @param session
     * @return
     */
    @PostMapping(value = "/create")
    public ResponseResult createSession(@RequestBody Sessions session){
        return sessionsService.createSession(session);
    }

    /**
     *删除会话
     * @return
     */
    @PostMapping(value = "/delete")
    public ResponseResult deleteSession(@RequestBody Sessions sessions){
        return sessionsService.deleteSessionById(sessions.getSessionId());
    }

    /**
     * 获取会话列表
     * @param messageId
     * @param receiverUserId
     * @param channelType
     * @param limit
     * @return
     */
    @GetMapping(value = "/records")
    public ResponseResult getMessageRecords(@RequestParam("messageId") String messageId,@RequestParam("receiverUserId") Long receiverUserId,@RequestParam("channelType") Integer channelType,@RequestParam("limit") Integer limit){
        return messagesService.getUserMessageRecords(messageId,receiverUserId,channelType,limit);
    }

    /**
     * 发送文本消息
     * @param sendMessageText
     * @return
     */
    @PostMapping(value = "/message/text")
    public ResponseResult sendMessageText(@RequestBody SendMessageTextDto sendMessageText){
        return messagesService.sendMessageText(sendMessageText);
    }

    /**
     * 发送图片接口
     * @param image
     * @param channelType
     * @param receiverId
     * @return
     */
    @PostMapping(value = "/message/image")
    @UploadFileCheck(message = "请选择文件类型为图片文件"
            ,type = UploadFileCheck.CheckType.SUFFIX
            ,supportedSuffixes = {"png", "jpg",  "jpeg"})
    public ResponseResult sendImageMessage(@RequestParam("image")MultipartFile image,@RequestParam("channelType") String channelType,@RequestParam("receiverId") String receiverId){
        return uploadService.sendImageMessage(image,channelType,receiverId);
    }

    @PostMapping("/message/file")
    public ResponseResult sendFileMessage(@RequestBody FileMessageDo fileMessage){
        return uploadService.sendFileMessage(fileMessage);
    }

    @GetMapping("/records/history")
    public ResponseResult getRecordsHistory(@RequestParam("messageId") String messageId,@RequestParam("receiverUserId") Long receiverUserId,@RequestParam("channelType") Integer channelType,@RequestParam("limit") Integer limit,@RequestParam("msgType") Integer msgType){
        return messagesService.getRecordsHistory(messageId,receiverUserId,channelType,limit,msgType);
    }

    /**
     * 设置消息为已读
     * @return
     */
    @PostMapping("/unread/clear")
    public ResponseResult unreadMessageClear(@RequestBody ClearMessage clearMessage){
        return messagesService.unreadMessageClear(clearMessage);
    }

    @PostMapping("/message/delete")
    public ResponseResult deleteMessage(@RequestBody ClearMessage deleteMessage){
        return messagesService.deleteMessage(deleteMessage);
    }

    @PostMapping("/message/revoke")
    public ResponseResult revokeMessage(@RequestBody Messages message){
        return messagesService.revokeMessage(message);
    }

    @PostMapping("/topping")
    public ResponseResult setSessionTop(@RequestBody Sessions sessions){
        return sessionsService.setSessionTop(sessions);
    }

    @PostMapping("/disturb")
    public ResponseResult setSessionDisturb(@RequestBody Sessions sessions){
        return sessionsService.setSessionDisturb(sessions);
    }
}