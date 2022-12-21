package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.LikesEntity;

/**
 * (Likes)表服务接口
 *
 * @author makejava
 * @since 2022-11-11 20:55:34
 */
public interface LikesService extends IService<LikesEntity> {
    R likes(Long articleId);

    R likesList(Long userId);
}
