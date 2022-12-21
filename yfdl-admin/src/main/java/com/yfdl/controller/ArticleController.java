package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.dto.ArticleDetailDto;
import com.yfdl.service.ArticleService;
import com.yfdl.vo.ArticleListVo;
import com.yfdl.vo.PageVo;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired private ArticleService articleService;

    @GetMapping("articleList")
    public R<PageVo<ArticleListVo>> articleList(@RequestParam(required = false) String title,
                                                @RequestParam(required = false) Character status,
                                                @RequestParam(required = false) Long categoryId ,
                                                @RequestParam(required = false) Long spanId,
                                                @RequestParam(defaultValue = "1") Long currentPage,
                                                @RequestParam(defaultValue = "10") Long pageSize){
        //查询对分类下的文章列表
        return articleService.articleList(title,status,categoryId,spanId,currentPage,pageSize, pageSize);
    }

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
}
