package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.ImEntity;
import com.yfdl.vo.im.ChatUserListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (Im)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-22 17:50:54
 */
@Mapper
public interface ImMapper extends BaseMapper<ImEntity> {
    ChatUserListVo[] chatUserList(@Param("userId") Long userId);

    ImEntity[] chatHistory(@Param("userId") Long userId, @Param("toId") Long toId);
}
