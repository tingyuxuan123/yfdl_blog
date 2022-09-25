package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.ArticleEntity;

import java.util.List;

/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2022-09-23 23:46:42
 */
public interface ArticleService extends IService<ArticleEntity> {
    R<List<ArticleEntity>> hotArticleList();
}
