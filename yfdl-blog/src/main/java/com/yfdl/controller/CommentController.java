package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public R commentList(@RequestParam Long pageNum,@RequestParam Long pageSize,@RequestParam Long articleId){
        return commentService.commentList(pageNum,pageSize,articleId);
    }
}
