package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.RoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-02 10:59:46
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenuEntity> {
}
