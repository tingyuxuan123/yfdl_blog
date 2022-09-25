package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类表(Category)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-25 13:47:24
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
}
