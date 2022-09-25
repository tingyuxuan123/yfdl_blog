package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.ArticleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章表(Article)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-23 23:45:46
 */
@Mapper
public interface ArticleMapper extends BaseMapper<ArticleEntity> {
}
