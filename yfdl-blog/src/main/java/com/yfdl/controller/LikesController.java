package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.service.LikesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api("文章点赞")
@RestController
@RequestMapping("likes")
public class LikesController {

    @Resource
    private LikesService likesService;

    @ApiOperation("点赞，取消点赞,需要登录")
    @GetMapping()
    public R likes(Long articleId){
        return likesService.likes(articleId);
    }

    @ApiOperation("获取点赞列表")
    @GetMapping("/likesList")
    public R likesList(Long userId){
        return likesService.likesList(userId);
    }
}
