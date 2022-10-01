package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-29 15:31:08
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuEntity> {
}
