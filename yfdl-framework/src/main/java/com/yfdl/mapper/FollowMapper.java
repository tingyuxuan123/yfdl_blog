package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.FollowEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Follow)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-08 22:26:51
 */
@Mapper
public interface FollowMapper extends BaseMapper<FollowEntity> {
}
