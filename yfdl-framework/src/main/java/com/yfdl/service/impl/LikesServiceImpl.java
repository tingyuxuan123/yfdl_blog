package com.yfdl.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.entity.FollowEntity;
import com.yfdl.service.ArticleService;
import  com.yfdl.service.LikesService;
import com.yfdl.mapper.LikesMapper;
import com.yfdl.entity.LikesEntity;
import com.yfdl.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (Likes)表服务实现类
 *
 * @author makejava
 * @since 2022-11-11 20:55:34
 */
@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, LikesEntity> implements LikesService {

    @Resource
    private ArticleService articleService;

    @Override
    public R likes(Long articleId) {

        Long userId = SecurityUtils.getUserId();

        //1.判断是否点赞

        LikesEntity likes = query().eq("user_id", userId).eq("like_article_id", articleId).one();

        if (ObjectUtil.isNull(likes)){
            //3.如果没有点赞，添加点赞
            LikesEntity likesEntity = new LikesEntity();
            likesEntity.setUserId(userId);
            likesEntity.setLikeArticleId(articleId);
            boolean b = save(likesEntity);

            if(b){
                articleService.update().eq("id",articleId).setSql("likes_count=likes_count+1").update();
            }

        }else {
            //2.如果点赞，取消点赞
            boolean b = removeById(likes.getId());
            if(b){
                articleService.update().eq("id",articleId).setSql("likes_count=likes_count-1").update();
            }
        }
        //4.返回操作后的状态
        return R.successResult();

    }

    @Override
    public R likesList(Long userId) {
      LikesEntity[] likesEntities = baseMapper.getLikesList(userId);
        return R.successResult(likesEntities);
    }


}
