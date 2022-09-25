package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表(SysUser)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-25 19:18:32
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {
}
