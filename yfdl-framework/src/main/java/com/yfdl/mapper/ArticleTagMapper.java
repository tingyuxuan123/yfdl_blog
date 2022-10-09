package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.ArticleTagEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-02 11:00:26
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTagEntity> {
}
