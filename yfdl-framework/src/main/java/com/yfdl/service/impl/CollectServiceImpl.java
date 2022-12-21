package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.dto.collect.InsertCollectDto;
import  com.yfdl.service.CollectService;
import com.yfdl.mapper.CollectMapper;
import com.yfdl.entity.CollectEntity;
import com.yfdl.utils.SecurityUtils;
import org.apache.poi.hssf.record.DVALRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (Collect)表服务实现类
 *
 * @author makejava
 * @since 2022-11-13 20:07:31
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, CollectEntity> implements CollectService {

    @Override
    public R insertCollect(InsertCollectDto insertCollectDto) {


        //根据id遍历要存的收藏夹id
        List<CollectEntity> collect =insertCollectDto.collectionId.stream().map((id) -> {
            CollectEntity collectEntity = new CollectEntity();
            collectEntity.setCollectionId(id);
            collectEntity.setArticleId(insertCollectDto.articleId);
            return collectEntity;
        }).collect(Collectors.toList());

        //保存
        boolean b = saveBatch(collect);
        if(b){
            return R.successResult();
        }else {
            return R.errorResult(400,"添加收藏失败");
        }

    }

    @Override
    public R cancelCollect(Long articleId) {
        Long userId = SecurityUtils.getUserId();

        QueryWrapper<CollectEntity> collectEntityQueryWrapper = new QueryWrapper<>();
        collectEntityQueryWrapper.eq("article_id",articleId).eq("create_by",userId);

        boolean remove = remove(collectEntityQueryWrapper);
        if(remove){
            return R.successResult();
        }else {
            return R.errorResult(400,"取消收藏失败");
        }
    }
}
