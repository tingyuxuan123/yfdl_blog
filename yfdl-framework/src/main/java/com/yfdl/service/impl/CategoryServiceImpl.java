package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.constants.SystemConstants;
import com.yfdl.entity.ArticleEntity;
import com.yfdl.service.ArticleService;
import com.yfdl.service.CategoryService;
import com.yfdl.mapper.CategoryMapper;
import com.yfdl.entity.CategoryEntity;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.vo.CategoryDetailListVo;
import com.yfdl.vo.CategoryListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-09-25 13:47:24
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public R<List<CategoryListVo>> getCategoryList() {
        //获取分类列表，必须要有文章才会返回分类

        //1只展示分类列表
        LambdaQueryWrapper<ArticleEntity> articleEntityLambdaQueryWrapper = new LambdaQueryWrapper<ArticleEntity>();
        articleEntityLambdaQueryWrapper.eq(ArticleEntity::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //获取或所有发布的文章
        List<ArticleEntity> articleEntityList = articleService.list(articleEntityLambdaQueryWrapper);

        //获取有发布文章的分类
        List<Long> categoryIds = articleEntityList.stream()
                .map(ArticleEntity::getCategoryId)
                .distinct()
                .collect(Collectors.toList());

        LambdaQueryWrapper<CategoryEntity> categoryEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //获取对应id的分类名
        categoryEntityLambdaQueryWrapper.in(CategoryEntity::getId,categoryIds);
        categoryEntityLambdaQueryWrapper.eq(CategoryEntity::getStatus,SystemConstants.CATEGORY_STATUS_NORMAL);
        List<CategoryEntity> list = list(categoryEntityLambdaQueryWrapper);

        List<CategoryListVo> categoryListVos = BeanCopyUtils.copyBeanList(list, CategoryListVo.class);


        return R.successResult(categoryListVos);
    }

    @Override
    public R getAllCategoryList() {  //返回所有分类列表
        List<CategoryEntity> list = list();
        List<CategoryListVo> categoryListVos = BeanCopyUtils.copyBeanList(list, CategoryListVo.class);

        return R.successResult(categoryListVos);
    }

    @Override
    public R allCategoryDetailList() {

        List<CategoryEntity> list = list();
        List<CategoryDetailListVo> categoryDetailListVos = BeanCopyUtils.copyBeanList(list, CategoryDetailListVo.class);

        categoryDetailListVos.forEach(categoryDetailListVo -> {
            LambdaQueryWrapper<ArticleEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ArticleEntity::getCategoryId,categoryDetailListVo.getId());
            int count = articleService.count(queryWrapper);
            categoryDetailListVo.setArticleCount((long) count);

        });
        return R.successResult(categoryDetailListVos);
    }


}
