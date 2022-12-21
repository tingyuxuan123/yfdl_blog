package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.LikesCommentEntity;

/**
 * (LikesComment)表服务接口
 *
 * @author makejava
 * @since 2022-11-15 22:56:09
 */
public interface LikesCommentService extends IService<LikesCommentEntity> {
    R likeCommentByArticle(Long articleId);
}
