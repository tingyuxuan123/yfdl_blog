package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.constants.SystemConstants;
import com.yfdl.entity.CommentEntity;
import com.yfdl.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public R commentList(@RequestParam Long pageNum,@RequestParam Long pageSize,@RequestParam Long articleId){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, pageNum,pageSize,articleId);
    }

    @PostMapping()
    public R comment(@RequestBody CommentEntity commentEntity){
        return commentService.comment(commentEntity);
    }

    /**
     * 友链评论
     */
    @GetMapping("/linkCommentList")
    public R linkCommentList(@RequestParam Long pageNum,@RequestParam Long pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,pageNum,pageSize,null);
    }

}
