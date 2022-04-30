package com.terry.pigeon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * 好友申请表(FriendRecords)表实体类
 *
 * @author makejava
 * @since 2022-03-03 21:48:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pigeonim_friend_records")
public class FriendRecords implements Serializable {
    private static final long serialVersionUID = -84525027039475987L;
        
    @TableId(value = "records_id",type = IdType.AUTO)
    private Integer recordsId;

    
    private Long userId;
    
    private Long friendId;
    //0 等待通过 1 已通过 2 已拒绝
    private Integer status;
    
    private Date createdAt;
    //请求信息
    private String information;

    private String rejectRemark;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String avatar;




}
