package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.entity.ArticleEntity;
import com.yfdl.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
