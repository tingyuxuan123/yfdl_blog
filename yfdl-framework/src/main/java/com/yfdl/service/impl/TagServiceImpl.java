package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.entity.ArticleTagEntity;
import com.yfdl.entity.CategoryEntity;
import com.yfdl.service.ArticleTagService;
import com.yfdl.service.TagService;
import com.yfdl.mapper.TagMapper;
import com.yfdl.entity.TagEntity;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.vo.PageVo;
import com.yfdl.vo.TagDetailListVo;
import com.yfdl.vo.TagListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-01 17:06:03
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, TagEntity> implements TagService {

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public R getTagList() {
        List<TagEntity> list = list();
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(list, TagListVo.class);
        return R.successResult(tagListVos);
    }

    @Override
    public R queryTagListByParams(Long currentPage, Long pageSize, String name) {
        Page<TagEntity> tagEntityPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<TagEntity> tagEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagEntityLambdaQueryWrapper.eq(Objects.nonNull(name),TagEntity::getName,name);
        page(tagEntityPage,tagEntityLambdaQueryWrapper);

        List<TagDetailListVo> tagDetailListVos = BeanCopyUtils.copyBeanList(tagEntityPage.getRecords(), TagDetailListVo.class);

        tagDetailListVos.stream().forEach(tagDetailListVo -> {
            LambdaQueryWrapper<ArticleTagEntity> articleTagEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagEntityLambdaQueryWrapper.eq(ArticleTagEntity::getTagId,tagDetailListVo.getId());
            int count = articleTagService.count(articleTagEntityLambdaQueryWrapper);
            tagDetailListVo.setArticleCount((long)count);
        });

        PageVo<TagDetailListVo> tagDetailListVoPageVo = new PageVo<>(tagDetailListVos, tagEntityPage.getTotal());

        return R.successResult(tagDetailListVoPageVo);

    }

    @Override
    public R insertTag(TagEntity tagEntity) {
        boolean save = save(tagEntity);
        return R.successResult();
    }

    @Override
    public R updateTag(TagEntity tagEntity) {

        boolean b = updateById(tagEntity);
        return R.successResult();
    }

    @Override
    public R deleteTag(Long id) {
        boolean b = removeById(id);
        return R.successResult();
    }


}
