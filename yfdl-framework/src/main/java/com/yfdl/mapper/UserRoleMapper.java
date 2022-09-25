package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-25 19:27:36
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {
}
