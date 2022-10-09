package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.CategoryEntity;
import com.yfdl.vo.CategoryListVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-09-25 13:47:24
 */
public interface CategoryService extends IService<CategoryEntity> {
    R<List<CategoryListVo>> getCategoryList();

    R getAllCategoryList();

    R allCategoryDetailList();
}
