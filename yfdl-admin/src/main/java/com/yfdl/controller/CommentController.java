package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @ApiOperation("获取需要审核的文章列表")
    @GetMapping("commentAuditList")
    public R articleAuditList(@RequestParam Long currentPage, @RequestParam Long pageSize,
                              @RequestParam(required = false) String userName,
                              @RequestParam(required = false) Integer audit
    ){
        return commentService.commentAuditList(currentPage,pageSize,userName,audit);
    }

    @ApiOperation("文章审核")
    @GetMapping("commentAudit")
    public R articleAudit(@RequestParam Long id ,@RequestParam Integer audit){
        return commentService.commentAudit(id,audit);
    }

}
