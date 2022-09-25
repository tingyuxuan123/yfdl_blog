package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.LinkEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 友链(Link)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-25 17:51:55
 */
@Mapper
public interface LinkMapper extends BaseMapper<LinkEntity> {
}
