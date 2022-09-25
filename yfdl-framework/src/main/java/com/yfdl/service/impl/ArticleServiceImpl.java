package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.yfdl.common.R;
import com.yfdl.constants.SystemConstants;
import com.yfdl.service.ArticleService;
import com.yfdl.mapper.ArticleMapper;
import com.yfdl.entity.ArticleEntity;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2022-09-23 23:48:37
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleEntity> implements ArticleService {
    @Override
    public R<List<ArticleEntity>> hotArticleList() {

        LambdaQueryWrapper<ArticleEntity> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ArticleEntity::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(ArticleEntity::getViewCount);
        Page<ArticleEntity> articleEntityPage = new Page<>(1, 10);
        Page<ArticleEntity> page = page(articleEntityPage, queryWrapper);

        List<ArticleEntity> articles=page.getRecords();
//        List<HotArticleVo> hotArticleVos =new ArrayList<>();
//        for (ArticleEntity article : articles) {
//            HotArticleVo hotArticleVo = new HotArticleVo();
//            BeanUtils.copyProperties(article,hotArticleVo);
//            hotArticleVos.add(hotArticleVo);
//        }
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);


        return R.successResult(hotArticleVos);
    }
}
