package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.constants.SystemConstants;
import com.yfdl.entity.CommentEntity;
import com.yfdl.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
@Api(tags = "评论")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation("获取评论列表")
    @GetMapping("/commentList")
    public R commentList(@RequestParam Long pageNum,@RequestParam Long pageSize,@RequestParam Long articleId){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, pageNum,pageSize,articleId);
    }

    @ApiOperation("发表评论")
    @PostMapping()
    public R comment(@RequestBody CommentEntity commentEntity){
        return commentService.comment(commentEntity);
    }

    /**
     * 友链评论
     */
    @ApiOperation(value = "友链评论列表",notes = "获取友链评论")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name="pageNum",value="页号"),
                    @ApiImplicitParam(name="pageSize",value="页面大小"),
            }
    )
    @GetMapping("/linkCommentList")
    public R linkCommentList(@RequestParam Long pageNum,@RequestParam Long pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,pageNum,pageSize,null);
    }

}
