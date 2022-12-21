package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.service.FollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "关注")
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Resource
    private FollowService followService;

    @ApiOperation("关注，或取消关注，需要登录")
    @GetMapping()
    public R follow(@RequestParam Long followId){
        return followService.follow(followId);
    }

    @ApiOperation("获取用户的关注列表")
    @GetMapping("/followList")
    public R followList(@RequestParam Long id){
        return followService.followList(id);
    }
}
