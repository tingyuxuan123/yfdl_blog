package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.CategoryEntity;
import com.yfdl.entity.TagEntity;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-10-01 17:06:03
 */
public interface TagService extends IService<TagEntity> {
    R getTagList();

    R queryTagListByParams(Long currentPage, Long pageSize, String name);

    R insertTag(TagEntity tagEntity);

    R updateTag(TagEntity tagEntity);

    R deleteTag(Long id);
}
