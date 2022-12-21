package com.yfdl.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.common.SystemException;
import com.yfdl.entity.LikesCommentEntity;
import com.yfdl.entity.UserEntity;
import com.yfdl.service.CommentService;
import com.yfdl.mapper.CommentMapper;
import com.yfdl.entity.CommentEntity;
import com.yfdl.service.LikesCommentService;
import com.yfdl.service.UserService;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.utils.SecurityUtils;
import com.yfdl.vo.CommentVo;
import com.yfdl.vo.PageVo;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-09-28 13:45:57
 */
@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {
    @Autowired
    private UserService userService;

    @Resource
    private LikesCommentService likesCommentService;

    @Override
    public R commentList(char commentType, Long pageNum, Long pageSize, Long articleId) {

        Page<CommentEntity> commentEntityPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CommentEntity> commentEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentEntityLambdaQueryWrapper.eq(!Objects.isNull(articleId),CommentEntity::getArticleId,articleId);
        // -1 代表根评论
        commentEntityLambdaQueryWrapper.eq(CommentEntity::getRootId,-1);

        commentEntityLambdaQueryWrapper.eq(CommentEntity::getType,commentType);

        page(commentEntityPage,commentEntityLambdaQueryWrapper);
        List<CommentEntity> commentList = commentEntityPage.getRecords();
        //获取到根评论
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(commentList, CommentVo.class);
        toCommentVoList(commentVos);

        //获取子评论
            commentVos.stream().forEach(commentVo -> {
                LambdaQueryWrapper<CommentEntity> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(CommentEntity::getRootId,commentVo.getId());
                List<CommentEntity> list = list(queryWrapper); //根评论下的子评论

                List<CommentVo> commentVosChildren = BeanCopyUtils.copyBeanList(list, CommentVo.class);
                toCommentVoList(commentVosChildren);
                commentVo.setChildren(commentVosChildren);
            });




        PageVo<CommentVo> commentVoPageVo = new PageVo<>(commentVos, commentEntityPage.getTotal());

        return R.successResult(commentVoPageVo);
    }

    /**
     * 给评论人和被评论人赋值
     * @param commentVos
     * @return
     */
    public void toCommentVoList(List<CommentVo> commentVos){

        commentVos.stream().forEach(commentVo -> {
            UserEntity user = userService.getById(commentVo.getCreateBy());
            commentVo.setUsername(user.getNickName());
            commentVo.setAvatar(user.getAvatar());

            if(commentVo.getToCommentUserId()!=-1){
                UserEntity userByComment  = userService.getById(commentVo.getToCommentUserId());
                commentVo.setToCommentUserName(userByComment.getNickName());
//                commentVo.setAvatar(userByComment.getAvatar());

            }

        });

}



    @Override
    public R comment(CommentEntity commentEntity) {
        if(!Strings.hasText(commentEntity.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }

        save(commentEntity);


        return R.successResult();
    }

    @Transactional
    @Override
    public R thumbsUp(Long commentId, Long articleId) {

        Long userId = SecurityUtils.getUserId();

        LikesCommentEntity entity = likesCommentService.query().eq("user_id", userId).eq("like_comment_id", commentId).one();



        if(ObjectUtil.isNotNull(entity)){
            //不为空，取消点赞
            boolean b =likesCommentService.removeById(entity.getId());

            if (b) { //添加点赞记录成功后
                //点赞数减1
                boolean up = update().setSql("likes_count=likes_count-1").eq("id", commentId).update();

            }

        }else {
            //为空，添加点赞。
            LikesCommentEntity likesCommentEntity = new LikesCommentEntity();
            likesCommentEntity.setArticleId(articleId);
            likesCommentEntity.setUserId(userId);
            likesCommentEntity.setLikeCommentId(commentId);
            boolean save = likesCommentService.save(likesCommentEntity);
            if (save) {
                boolean up = update().setSql("likes_count=likes_count+1").eq("id", commentId).update();
            }
        }

        return R.successResult();
    }


}

