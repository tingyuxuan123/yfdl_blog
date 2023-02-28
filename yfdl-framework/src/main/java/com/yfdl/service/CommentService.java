package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.CommentEntity;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-09-28 13:45:57
 */
public interface CommentService extends IService<CommentEntity> {
    R commentList(char commentType, Long pageNum, Long pageSize,Long articleId);

    R comment(CommentEntity commentEntity);

    R thumbsUp(Long commentId, Long articleId);

    R commentAuditList(Long currentPage, Long pageSize, String userName, Integer audit);

    R commentAudit(Long id, Integer audit);
}
