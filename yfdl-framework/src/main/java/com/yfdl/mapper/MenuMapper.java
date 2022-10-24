package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-02 10:59:09
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuEntity> {
    List<String> selectPermsByUserId(Long userId);

    List<MenuEntity> selectRootMenuByUserId(Long userId);

    List<MenuEntity> getChildren(@Param("userId") Long userId, @Param("id") Long id);
}
