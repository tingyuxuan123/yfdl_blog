package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.service.CommentService;
import com.yfdl.mapper.CommentMapper;
import com.yfdl.entity.CommentEntity;
import com.yfdl.service.UserService;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.vo.CommentVo;
import com.yfdl.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public R commentList(Long pageNum, Long pageSize, Long articleId) {

        Page<CommentEntity> commentEntityPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CommentEntity> commentEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentEntityLambdaQueryWrapper.eq(CommentEntity::getArticleId,articleId);
        // -1 代表根评论
        commentEntityLambdaQueryWrapper.eq(CommentEntity::getRootId,-1);

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
          commentVo.setUsername(userService.getById(commentVo.getCreateBy()).getNickName());


            if(commentVo.getToCommentUserId()!=-1){
              commentVo.setToCommentUserName(userService.getById(commentVo.getToCommentUserId()).getUserName());
          }

        });

}

}

