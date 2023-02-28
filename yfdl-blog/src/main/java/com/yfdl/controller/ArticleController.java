package com.yfdl.controller;

import com.yfdl.annotation.SystemLog;
import com.yfdl.common.R;
import com.yfdl.entity.ArticleEntity;
import com.yfdl.service.ArticleService;
import com.yfdl.vo.ArticleListVo;
import com.yfdl.vo.ArticleVo;
import com.yfdl.vo.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation("获取观看量最多的10篇文章")
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
                                                @RequestParam(required = false) Long userId,
                                                @RequestParam(defaultValue = "1") Long currentPage,
                                                @RequestParam(defaultValue = "10") Long pageSize){
        //查询对分类下的文章列表
        return articleService.articleList(title,status,categoryId,spanId,userId,currentPage,pageSize);

    }

    @ApiOperation("根据文章条件获取文章列表")
    @GetMapping("articleListByUserId")
    public R<PageVo<ArticleListVo>> articleListByUserId(ArticleEntity articleEntity, @RequestParam(defaultValue = "1") Long currentPage,
                                                        @RequestParam(defaultValue = "10") Long pageSize){
        return articleService.articleListByUserId(articleEntity,currentPage,pageSize);
    }


    @GetMapping("{id}")
    public R<ArticleVo> article(HttpServletRequest httpServletRequest, @PathVariable Long id){
        //获取文章详情
        return articleService.article(httpServletRequest,id);
    }

    @SystemLog(businessName = "更新文章观看次数")
    @PutMapping("updateViewCount/{id}")
    public R updateViewCount(@PathVariable Long id){
        return articleService.updateViewCount(id);
    }


    @ApiOperation("根据用户id获取点赞文章列表")
    @GetMapping("articleListByUserLikes")
    public R articleListByUserLikes(@RequestParam Long userId){
        return articleService.articleListByUserLikes(userId);
    }

    @ApiOperation("获取推荐文章列表")
    @GetMapping("articleListByRecommended")
    public R articleListByRecommended(@RequestParam(defaultValue = "1") Long currentPage,
                                      @RequestParam(defaultValue = "10") Long pageSize){

        return articleService.articleListByRecommended(currentPage,pageSize);
    }

    @ApiOperation("获取最新文章列表")
    @GetMapping("articleListByNew")
    public R articleListByNew(@RequestParam(defaultValue = "1") Long currentPage,
                              @RequestParam(defaultValue = "10") Long pageSize){
        return articleService.articleListByNew(currentPage,pageSize);
    }

    @ApiOperation("获取用户关注的作者发布的文章")
    @GetMapping("articleListByDynamic")
    public R articleListByDynamic(@RequestParam(defaultValue = "1") Long currentPage,
                                  @RequestParam(defaultValue = "10") Long pageSize){

        return articleService.articleListByDynamic(currentPage,pageSize);
    }

    @ApiOperation("根据关键词搜索文章")
    @GetMapping("searchArticle")
    public R searchArticle(@RequestParam(defaultValue = "1") Long currentPage,
                           @RequestParam(defaultValue = "10") Long pageSize , @RequestParam String searchParams){

        return articleService.searchArticle(currentPage,pageSize,searchParams);
    }

    @ApiOperation("获取总的文章数和评论数")
    @GetMapping("articleAndCommentCountInfo")
    public R articleAndCommentCountInfo(){
        return articleService.articleAndCommentCountInfo();
    }

    @ApiOperation("根据标签获取文章")
    @GetMapping("articeListbyTag")
    public R articleListByTag(@RequestParam(defaultValue = "1") Long currentPage,
                              @RequestParam(defaultValue = "10") Long pageSize,@RequestParam Long spanId){
        return articleService.articleListByTag(spanId,currentPage,pageSize);
    }


    @ApiOperation("根据收藏夹id获取收藏夹文章")
    @GetMapping("articleListByCollection")
    public R articleListByCollection(@RequestParam(defaultValue = "1") Long currentPage,
                                     @RequestParam(defaultValue = "10") Long pageSize,@RequestParam Long collectionId){
        return articleService.articleListByCollection(currentPage,pageSize,collectionId);
    }

    @ApiOperation("相爱文章推荐")
    @GetMapping("articleListByRelated")
    public R articleListByRelated(@RequestParam String articleTitle){
        return  articleService.articleListByRelated(articleTitle);
    }

    @ApiOperation("根据文章id获取文章")
    @GetMapping("articleListByCategoryId")
    public R articleListByCategoryId(@RequestParam(defaultValue = "1") Long currentPage,
                                     @RequestParam(defaultValue = "10") Long pageSize,@RequestParam Long categoryId){
        return articleService.articleListByCategoryId(currentPage,pageSize,categoryId);
    }

}
