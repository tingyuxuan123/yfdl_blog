package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.dto.ArticleDetailDto;
import com.yfdl.service.ArticleService;
import com.yfdl.vo.ArticleListVo;
import com.yfdl.vo.PageVo;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired private ArticleService articleService;


    @ApiModelProperty("后台文章列表只能获取对应的id的")
    @GetMapping("adminArticleList")
    public R<PageVo<ArticleListVo>> adminArticleList(@RequestParam(required = false) String title,
                                                @RequestParam(required = false) Character status,
                                                @RequestParam(required = false) Long categoryId ,
                                                @RequestParam(required = false) Long spanId,
                                                @RequestParam(defaultValue = "1") Long currentPage,
                                                @RequestParam(defaultValue = "10") Long pageSize){
        //查询对分类下的文章列表
        return articleService.adminArticleList(title,status,categoryId,spanId,currentPage,pageSize);
    }

    @ApiOperation("后台管理员的文章管理列表")
    @GetMapping("adminAllArticleList")
    public R  adminAllArticleList(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) Long categoryId ,
                                  @RequestParam(required = false) Long spanId,
                                  @RequestParam(defaultValue = "1") Long currentPage,
                                  @RequestParam(defaultValue = "10") Long pageSize){

        return articleService.adminAllArticleList(title,categoryId,spanId,currentPage,pageSize);


    }


    @GetMapping("articleInfo")
    public R article(HttpServletRequest httpServletRequest , @RequestParam Long id){
        return articleService.article(httpServletRequest, id);
    }

    @PostMapping("updateArticle")
    public R updateArticle(@RequestBody ArticleDetailDto articleDetailDto){
        return articleService.updateArticle(articleDetailDto);
    }

    @PostMapping("insertArticle")
    public R insertArticle(@RequestBody ArticleDetailDto articleDetailDto){
        return articleService.insertArticle(articleDetailDto);
    }

    @GetMapping("deleteArticle")
    public R deleteArticle(@RequestParam Long id){
        return R.successResult(articleService.removeById(id));
    }

    @ApiOperation("获取需要审核的文章列表")
    @GetMapping("articleAuditList")
    public R articleAuditList(@RequestParam Long currentPage,@RequestParam Long pageSize,
                              @RequestParam(required = false) String userName,
                              @RequestParam(required = false) Integer audit
        ){
        return articleService.articleAuditList(currentPage,pageSize,userName,audit);
    }

    @ApiOperation("文章是否推荐操作")
    @GetMapping("articleRecommend")
    public R articleRecommend(@RequestParam Long id, @RequestParam String isTop){
        return articleService.articleRecommend(id,isTop);
    }

    @ApiOperation("文章审核")
    @GetMapping("articleAudit")
    public R articleAudit(@RequestParam Long id ,@RequestParam Integer audit){
        return articleService.articleAudit(id,audit);
    }

    @ApiOperation("查看文章内容")
    @GetMapping("articleContent")
    public R articleContent(@RequestParam Long id){
        return articleService.articleContent(id);
    }

}
