package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.service.LikesCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api("评论点赞")
@RestController
@RequestMapping("LikesComment")
public class LikesCommentController {

    @Resource
    private LikesCommentService likesCommentService;

    @ApiOperation("根据文章id 获取用户评论点赞列表,需要登录")
    @GetMapping("likeCommentByArticle")
    public R likeCommentByArticle(@RequestParam Long articleId){
        return likesCommentService.likeCommentByArticle(articleId);
    }

}
