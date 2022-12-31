package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.ArticleEntity;
import com.yfdl.vo.ArticleListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文章表(Article)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-23 23:45:46
 */
@Mapper
public interface ArticleMapper extends BaseMapper<ArticleEntity> {
    ArticleListVo[] articleListByUserLikes(Long userId);

    ArticleListVo[] articleListByRecommended(@Param("skipNumber") Long skipNumber, @Param("pageSize") Long pageSize);

    Long getCount();

    ArticleListVo[] articleListByNew(@Param("skipNumber") Long skipNumber, @Param("pageSize") Long pageSize);
}
