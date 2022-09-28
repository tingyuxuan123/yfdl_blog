package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-28 13:45:57
 */
@Mapper
public interface CommentMapper extends BaseMapper<CommentEntity> {
}
