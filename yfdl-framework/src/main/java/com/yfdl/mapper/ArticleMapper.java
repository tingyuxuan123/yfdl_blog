package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.ArticleEntity;
import com.yfdl.vo.ArticleListVo;
import com.yfdl.vo.article.ArticleAudit;
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

    ArticleAudit[] articleAuditList(@Param("skipNumber") Long skipNumber, @Param("pageSize") Long pageSize, @Param("userId") Long userId, @Param("audit") Integer audit);

    ArticleListVo[] searchArticleList(@Param("skipNumber") Long skipNumber, @Param("pageSize") Long pageSize, @Param("searchParams") String searchParams);

    ArticleListVo[] articleListByDynamic(@Param("skipNumber") Long skipNumber, @Param("pageSize") Long pageSize,@Param("userId") Long userId);

    ArticleListVo[] articleListByTag(@Param("skipNumber") Long skipNumber, @Param("pageSize") Long pageSize,@Param("spanId") Long spanId);

    ArticleListVo[] adminAllArticleList(@Param("skipNumber") Long skipNumber, @Param("pageSize") Long pageSize,@Param("title") String title,@Param("categoryId") Long categoryId,@Param("spanId") Long spanId);

    ArticleListVo[] articleListByCollection(@Param("skipNumber") Long skipNumber, @Param("pageSize") Long pageSize, @Param("collectionId") Long collectionId);

    ArticleListVo[] articleListByRelated(@Param("articleTitle") String articleTitle);

    ArticleListVo[] articleListByCategoryId(@Param("skipNumber") Long skipNumber, @Param("pageSize") Long pageSize, @Param("categoryId") Long categoryId);
}
