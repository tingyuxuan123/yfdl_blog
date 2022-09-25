package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.entity.ArticleEntity;
import com.yfdl.service.ArticleService;
import com.yfdl.vo.ArticleListVo;
import com.yfdl.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("hotArticleList")
    public R<List<ArticleEntity>> hotArticleList(){
        //查询热门文章
        return articleService.hotArticleList();
    }

    @GetMapping("articleList")
    public R<PageVo<ArticleListVo>> articleList(@RequestParam(required = false) Long categoryId , @RequestParam(defaultValue = "1") Long currentPage, @RequestParam(defaultValue = "10") Long pageSize){
        //查询对分类下的文章列表

        return articleService.articleList(categoryId,currentPage,pageSize);


    }





}
