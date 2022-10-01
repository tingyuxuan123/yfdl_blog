package com.yfdl.job;

import com.yfdl.entity.ArticleEntity;
import com.yfdl.service.ArticleService;
import com.yfdl.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob{

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;


  //  @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 0 17 * * ?")  //每天五点更新一次
    public void updateViewCount(){
        //获取缓存中的值
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");

        List<ArticleEntity> articleEntities = viewCountMap.entrySet().stream()
                .map(stringIntegerEntry -> {
                    ArticleEntity articleEntity = new ArticleEntity();
                    articleEntity.setId(Long.valueOf(stringIntegerEntry.getKey()));
                    articleEntity.setViewCount(stringIntegerEntry.getValue().longValue());
                    return articleEntity;
                }).collect(Collectors.toList());

        //更新数据库
        articleService.updateBatchById(articleEntities);
    }

}
