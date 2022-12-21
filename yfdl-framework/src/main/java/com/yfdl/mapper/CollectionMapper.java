package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.CollectionEntity;
import com.yfdl.vo.collection.CollectionListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Collection)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-13 20:06:33
 */
@Mapper
public interface CollectionMapper extends BaseMapper<CollectionEntity> {
   List<CollectionListVo> collectionList(@Param("userId") Long userId);
}
