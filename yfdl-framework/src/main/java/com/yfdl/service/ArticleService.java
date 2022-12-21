package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.dto.ArticleDetailDto;
import com.yfdl.entity.ArticleEntity;
import com.yfdl.vo.ArticleListVo;
import com.yfdl.vo.ArticleVo;
import com.yfdl.vo.PageVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2022-09-23 23:46:42
 */
public interface ArticleService extends IService<ArticleEntity> {
    R<List<ArticleEntity>> hotArticleList();


    R<PageVo<ArticleListVo>> articleList(String title, Character status, Long categoryId, Long spanId, Long userId, Long currentPage, Long pageSize);

    R<ArticleVo> article(HttpServletRequest httpServletRequest, Long id);

    R updateViewCount(Long id);

    R updateArticle(ArticleDetailDto articleDetailDto);

    R insertArticle(ArticleDetailDto articleDetailDto);

    R<PageVo<ArticleListVo>> adminArticleList(String title, Character status, Long categoryId, Long spanId, Long currentPage, Long pageSize);

    R<PageVo<ArticleListVo>> articleListByUserId(ArticleEntity userId, Long currentPage, Long pageSize);
}
