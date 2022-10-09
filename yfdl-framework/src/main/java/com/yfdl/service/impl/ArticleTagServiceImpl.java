package com.yfdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.service.ArticleTagService;
import com.yfdl.mapper.ArticleTagMapper;
import com.yfdl.entity.ArticleTagEntity;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-02 11:00:26
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTagEntity> implements ArticleTagService {
}
