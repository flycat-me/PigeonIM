package com.terry.pigeon.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.terry.pigeon.common.constants.SystemConstants;
import com.terry.pigeon.common.enums.AppHttpCodeEnum;
import com.terry.pigeon.common.enums.MessageFileType;
import com.terry.pigeon.common.enums.TalkEventEnum;
import com.terry.pigeon.common.response.ResponseResult;
import com.terry.pigeon.entity.*;
import com.terry.pigeon.mapper.*;
import com.terry.pigeon.server.WebSocketChatServer;
import com.terry.pigeon.service.ObsUploadService;
import com.terry.pigeon.utils.ObsFileUpload;
import com.terry.pigeon.utils.RedisCache;
import com.terry.pigeon.utils.UploadFileUtils;
import com.terry.pigeon.utils.UploadPathUtils;
import com.terry.pigeon.vo.InitializationFile;
import com.terry.pigeon.vo.MergeFile;
import com.terry.pigeon.vo.frontData.FileMessageDo;
import com.terry.pigeon.vo.frontData.FileUploadInitialization;
import com.terry.pigeon.vo.message.TalkMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author terry_lang
 * @description 文件上传实现类
 * @since 2022-03-25 21:08
 **/
@Service
public class ObsUploadServiceImpl implements ObsUploadService {

    public static String fileName;

    //private static List<PartEtag> partEtags;
    @Autowired
    private ObsFileUpload obsFileUpload;

    @Resource
    private UsersMapper usersMapper;

    @Autowired
    private RedisCache redisCache;

    @Resource
    private SessionsMapper sessionsMapper;

    @Resource
    private MessageFileMapper messageFileMapper;

    @Resource
    private MessagesMapper messagesMapper;

    @Resource
    private WebSocketChatServer webSocketChatServer;

    @Resource
    private GroupUsersMapper groupsUserMapper;


    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) throws IOException {

        String fileName = multipartFile.getOriginalFilename();
        String path = obsFileUpload.uploadFileToObs(fileName,multipartFile.getInputStream());
        //更新数据库内的用户头像
        Users user = FriendsServiceImpl.getCurUser();
        user.setAvatar(path);
        redisCache.setCacheObject(user.getUserId().toString(),user);
        int update = usersMapper.updateById(user);
        if (update > 0){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("avatar",path);
            return ResponseResult.successResult(jsonObject);
        }else {
            return ResponseResult.successResult(AppHttpCodeEnum.UPDATE_AVATAR_FAIL);
        }

    }

    @Override
    public ResponseResult sendImageMessage(MultipartFile image,String channelType,String receiverId) {

        //上传图片文件
        String originalName = image.getOriginalFilename();
        long size = image.getSize();
        int index= originalName.lastIndexOf(".");
        String suffix = originalName.substring(index + 1);
        Long userId = FriendsServiceImpl.getCurUser().getUserId();
        String path = "";
        try {
            path = obsFileUpload.uploadImageToObs(originalName,image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Long sessionId = sessionsMapper.selectOne(new LambdaQueryWrapper<Sessions>().eq(Sessions::getUserId, userId)
                                                                                    .eq(Sessions::getReceiverId, receiverId)
                                                                                    .select(Sessions::getSessionId))
                                       .getSessionId();
        Messages messages = new Messages();
        messages.setSessionId(sessionId);
        messages.setMsgType(2);
        messages.setChannelType(Integer.valueOf(channelType));
        messages.setReceiverUserId(Long.valueOf(receiverId));
        messages.setCreatedAt(new Date());
        messages.setUpdateAt(new Date());
        messages.setFromUserId(userId);
        messages.setMsgText("[图片消息]");
        messages.setAvatar(FriendsServiceImpl.getCurUser()
                                             .getAvatar());
        messagesMapper.insert(messages);

        int messageId= messages.getMessageId();

        MessageFile messageFile = new MessageFile();
        messageFile.setMessageId(messageId);
        messageFile.setCreateAt(new Date());
        messageFile.setOriginalName(originalName);
        messageFile.setSize((int) size);
        messageFile.setSuffix(suffix);
        messageFile.setUrl(path);
        messageFile.setType(MessageFileType.IMAGE_TYPE.getFileType());
        messageFile.setUserId(userId);


        messageFileMapper.insert(messageFile);
        //写入数据库
        //获取sessionId
        //先写入message表
        //再写入file表
        messages.setFile(messageFile);
        TalkMessage talkMessage = new TalkMessage(TalkEventEnum.EVENT_TALK.getEvent(), messages);
        if (messages.getChannelType() == 1) {
            webSocketChatServer.sendMessageToUser(String.valueOf(userId), talkMessage);
            webSocketChatServer.sendMessageToUser(receiverId, talkMessage);
        }else {
            List<String> userIdLists = getGroupUserList(messages.getReceiverUserId());
            webSocketChatServer.sendMessageToAll(userIdLists,talkMessage);
        }
        //使用websocket将消息推送出去
        return ResponseResult.successResult();
    }

    /**
     * 分片文件上传初始化
     * @return uploadId
     */
    @Override
    public ResponseResult upLoadInitiate(FileUploadInitialization init) {

        Long splitSize = 2 * 1024 * 1024L;

        double splitNum = Math.ceil(Long.valueOf(init.getFileSize()) / splitSize);

        ObsUploadServiceImpl.fileName = UploadPathUtils.generateFilePath(init.getFileName());


        String uploadId = null;
        try {

            int index= init.getFileName().lastIndexOf(".");
            String suffix = init.getFileName().substring(index + 1);

            uploadId = UploadFileUtils.initializeShard(fileName);
            MessageFile messageFile = new MessageFile();
            messageFile.setUrl(SystemConstants.OBS_PATH+fileName);
            messageFile.setOriginalName(init.getFileName());
            messageFile.setSuffix(suffix);
            messageFile.setSize(Integer.valueOf(init.getFileSize()));
            messageFile.setCreateAt(new Date());
            messageFile.setUserId(FriendsServiceImpl.getCurUser()
                                                    .getUserId());
            redisCache.setCacheObject(uploadId,messageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InitializationFile initializationFile = new InitializationFile(uploadId,splitSize.toString());
        return ResponseResult.successResult(initializationFile);
    }

    @Override
    public ResponseResult uploadFileMultipart(MultipartFile file, String uploadId, String splitIndex, String splitNum) {
        try {
            UploadFileUtils.fragmentationUploadFile(uploadId,Integer.valueOf(splitIndex), Integer.valueOf(splitNum),fileName,file.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        MergeFile mergeFile = new MergeFile();
        if ((Integer.valueOf(splitIndex) + 1) == Integer.valueOf(splitNum)){
//            ObsFileUpload.mergeFiles(fileName,uploadId);
            mergeFile.setIsMerge("true");
            mergeFile.setUploadId(uploadId);
            ResponseResult.successResult(mergeFile);
        }

        mergeFile.setIsMerge("false");
        return ResponseResult.successResult(mergeFile);
    }

    @Override
    public ResponseResult sendFileMessage(FileMessageDo fileMessage) {


        Users user = FriendsServiceImpl.getCurUser();

        Long sessionId = sessionsMapper.selectOne(new LambdaQueryWrapper<Sessions>().eq(Sessions::getUserId, user.getUserId())
                                                                                    .eq(Sessions::getReceiverId, fileMessage.getReceiverId())
                                                                                    .select(Sessions::getSessionId))
                                       .getSessionId();
        Messages messages = new Messages();
        messages.setSessionId(sessionId);
        messages.setMsgType(2);
        messages.setChannelType(Integer.valueOf(fileMessage.getChannelType()));
        messages.setReceiverUserId(Long.valueOf(fileMessage.getReceiverId()));
        messages.setCreatedAt(new Date());
        messages.setUpdateAt(new Date());
        messages.setFromUserId(user.getUserId());
        messages.setAvatar(user.getAvatar());
        messages.setMsgText("[文件消息]");
        messagesMapper.insert(messages);

        int messageId= messages.getMessageId();

        MessageFile messageFile = redisCache.getCacheObject(fileMessage.getUploadId());
        messageFile.setType(MessageFileType.FILE_TYPE.getFileType());
        messageFile.setMessageId(messageId);

        messageFileMapper.insert(messageFile);
        //写入数据库
        //获取sessionId
        //先写入message表
        //再写入file表
        messages.setFile(messageFile);
        TalkMessage talkMessage = new TalkMessage(TalkEventEnum.EVENT_TALK.getEvent(), messages);

        if (messages.getChannelType() == 1) {
            webSocketChatServer.sendMessageToUser(String.valueOf(user.getUserId()),talkMessage);
            webSocketChatServer.sendMessageToUser(fileMessage.getReceiverId(),talkMessage);
        }else {
            List<String> userIdLists = getGroupUserList(messages.getReceiverUserId());
            webSocketChatServer.sendMessageToAll(userIdLists,talkMessage);
        }
        //使用websocket将消息推送出去
        return ResponseResult.successResult();
    }

    private List<String> getGroupUserList(Long groupId){
        List<GroupUsers> userIdList = groupsUserMapper.selectList(new LambdaQueryWrapper<GroupUsers>()
                                                    .eq(GroupUsers::getGroupId,groupId)
                                                    .select(GroupUsers::getUserId));

        List<String> groupUserList = userIdList.stream()
                                       .map(user -> user.getUserId().toString())
                                       .collect(Collectors.toList());
        return groupUserList;
    }


}
