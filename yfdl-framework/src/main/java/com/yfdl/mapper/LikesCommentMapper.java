package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.LikesCommentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * (LikesComment)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-15 22:56:09
 */
@Mapper
public interface LikesCommentMapper extends BaseMapper<LikesCommentEntity> {
}
