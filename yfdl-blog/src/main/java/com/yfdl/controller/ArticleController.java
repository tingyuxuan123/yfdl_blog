package com.yfdl.controller;

import com.yfdl.annotation.SystemLog;
import com.yfdl.common.R;
import com.yfdl.entity.ArticleEntity;
import com.yfdl.service.ArticleService;
import com.yfdl.vo.ArticleListVo;
import com.yfdl.vo.ArticleVo;
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
    public R<PageVo<ArticleListVo>> articleList(@RequestParam(required = false) String title,
                                                @RequestParam(required = false) Character status,
                                                @RequestParam(required = false) Long categoryId ,
                                                @RequestParam(required = false) Long spanId,
                                                @RequestParam(defaultValue = "1") Long currentPage,
                                                @RequestParam(defaultValue = "10") Long pageSize){
        //查询对分类下的文章列表
        return articleService.articleList(title,status,categoryId,spanId,currentPage,pageSize);

    }

    @GetMapping("{id}")
    public R<ArticleVo> article(@PathVariable Long id){
        //获取文章详情
        return articleService.article(id);
    }

    @SystemLog(businessName = "更新文章观看次数")
    @PutMapping("updateViewCount/{id}")
    public R updateViewCount(@PathVariable Long id){
        return articleService.updateViewCount(id);
    }


}
