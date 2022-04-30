package com.terry.pigeon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * 好友表(Friends)表实体类
 *
 * @author makejava
 * @since 2022-03-03 21:44:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pigeonim_friends")
public class Friends implements Serializable {
    private static final long serialVersionUID = -53882477438351098L;
        
    @TableId(type = IdType.AUTO)
    private Integer fId;

    
    private Long userId;
    
    private Long friendUserId;
    //好友名称
    private String friendName;
    
    private Date createdAt;
    
    private String friendRemark;
    
    private Date updateAt;
    //0.未置顶 1.已置顶
    private Integer delFlag;

    private Integer isOnline;



}
