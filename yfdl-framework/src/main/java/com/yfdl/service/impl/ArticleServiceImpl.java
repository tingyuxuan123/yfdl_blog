package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.constants.SystemConstants;
import com.yfdl.entity.CategoryEntity;
import com.yfdl.service.ArticleService;
import com.yfdl.mapper.ArticleMapper;
import com.yfdl.entity.ArticleEntity;
import com.yfdl.service.CategoryService;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.vo.ArticleListVo;
import com.yfdl.vo.ArticleVo;
import com.yfdl.vo.HotArticleVo;
import com.yfdl.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2022-09-23 23:48:37
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleEntity> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

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

    @Override
    public R<PageVo<ArticleListVo>> articleList(Long categoryId, Long currentPage, Long pageSize) {

        LambdaQueryWrapper<ArticleEntity> queryWrapper = new LambdaQueryWrapper<ArticleEntity>();
        queryWrapper.eq(ArticleEntity::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,ArticleEntity::getCategoryId,categoryId);
        queryWrapper.orderByDesc(ArticleEntity::getIsTop);
        Page<ArticleEntity> articleEntityPage = new Page<>(currentPage,pageSize);
        page(articleEntityPage,queryWrapper); //查询结果会存入articleEntityPage

        List<ArticleEntity> articleEntities = articleEntityPage.getRecords();

        //获取分类id对应的分类名称
         articleEntities= articleEntities.stream().peek(articleEntity -> {
            CategoryEntity categoryEntity = categoryService.getById(articleEntity.getCategoryId());
            String name = categoryEntity.getName();
            articleEntity.setCategoryName(name);
        }).collect(Collectors.toList());


        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleEntities, ArticleListVo.class);


        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos, articleEntityPage.getTotal());

        return R.successResult(articleListVoPageVo);
    }

    @Override
    public R<ArticleVo> article(Long id) {

        ArticleEntity articleEntity = getById(id);

        CategoryEntity category = categoryService.getById(articleEntity.getCategoryId());
        articleEntity.setCategoryName(category.getName());

        ArticleVo articleVo = BeanCopyUtils.copyBean(articleEntity, ArticleVo.class);

        return R.successResult(articleVo);
    }


}
