package com.yfdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import  com.yfdl.service.CollectionService;
import com.yfdl.mapper.CollectionMapper;
import com.yfdl.entity.CollectionEntity;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.utils.SecurityUtils;
import com.yfdl.vo.collection.CollectionListVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Collection)表服务实现类
 *
 * @author makejava
 * @since 2022-11-13 20:06:33
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, CollectionEntity> implements CollectionService {
    @Override
    public R collectionList() {

        Long userId = SecurityUtils.getUserId();

        List<CollectionListVo> collectionListVos = baseMapper.collectionList(userId);

        return R.successResult(collectionListVos);
    }

    @Override
    public R createCollection(CollectionEntity collectionEntity) {

        Long userId = SecurityUtils.getUserId();
        collectionEntity.setUserId(userId);
        boolean save = save(collectionEntity);

        if(save){
            return R.successResult();
        }

        return R.errorResult(400,"失败");


    }

    @Override
    public R updateCollection(CollectionEntity collectionEntity) {
        boolean b = updateById(collectionEntity);
        return R.successResult();
    }

    @Override
    public R deleteCollection(Long collectionId) {
        return R.successResult(removeById(collectionId));
    }


}
