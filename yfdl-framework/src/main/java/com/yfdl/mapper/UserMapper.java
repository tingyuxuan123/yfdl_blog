package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-25 19:28:22
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
