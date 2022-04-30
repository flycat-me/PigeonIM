package com.terry.pigeon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.terry.pigeon.entity.Users;


/**
 * 用户表(Users)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-02 17:18:48
 */
public interface UsersMapper extends BaseMapper<Users> {
    public Users getByUserIdString();

}
