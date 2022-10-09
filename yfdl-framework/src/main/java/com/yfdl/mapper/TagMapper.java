package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.TagEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-01 17:06:03
 */
@Mapper
public interface TagMapper extends BaseMapper<TagEntity> {
}
