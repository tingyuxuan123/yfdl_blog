package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-02 15:45:30
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {
    List<String> selectRoleKeyByUserId(Long userId);
}
