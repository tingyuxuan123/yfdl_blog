package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.LikesEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Likes)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-11 20:55:34
 */
@Mapper
public interface LikesMapper extends BaseMapper<LikesEntity> {
    LikesEntity[] getLikesList(Long userId);
}
