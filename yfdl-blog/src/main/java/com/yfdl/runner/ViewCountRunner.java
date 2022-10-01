package com.yfdl.runner;

import com.yfdl.entity.ArticleEntity;
import com.yfdl.service.ArticleService;
import com.yfdl.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  运行时启动，把文章的流量量存入redis
 */
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //获取所有文章
        List<ArticleEntity> list = articleService.list();
        //获取id: 观看量的键值对
        Map<String,Integer> viewCountMap=list.stream()
                .collect(Collectors.toMap(articleEntity -> articleEntity.getId().toString(),articleEntity -> articleEntity.getViewCount().intValue()));

        redisCache.setCacheMap("article:viewCount",viewCountMap);

    }
}
