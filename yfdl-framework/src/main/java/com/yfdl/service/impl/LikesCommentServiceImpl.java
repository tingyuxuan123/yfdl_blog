package com.yfdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import  com.yfdl.service.LikesCommentService;
import com.yfdl.mapper.LikesCommentMapper;
import com.yfdl.entity.LikesCommentEntity;
import com.yfdl.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (LikesComment)表服务实现类
 *
 * @author makejava
 * @since 2022-11-15 22:56:09
 */
@Service
public class LikesCommentServiceImpl extends ServiceImpl<LikesCommentMapper, LikesCommentEntity> implements LikesCommentService {
    @Override
    public R likeCommentByArticle(Long articleId) {
        Long userId = SecurityUtils.getUserId();

        //根据文章id查询文章中用户点赞的评论
        List<LikesCommentEntity> list = lambdaQuery().eq(LikesCommentEntity::getArticleId, articleId).eq(LikesCommentEntity::getUserId, userId).list();
        //返回点赞的评论id
        List<Long> list1 = list.stream().map(LikesCommentEntity::getLikeCommentId).collect(Collectors.toList());
        return R.successResult(list1);
    }
}
