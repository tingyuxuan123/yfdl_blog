package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.CollectionEntity;

/**
 * (Collection)表服务接口
 *
 * @author makejava
 * @since 2022-11-13 20:06:33
 */
public interface CollectionService extends IService<CollectionEntity> {
    R collectionList();

    R createCollection(CollectionEntity collectionEntity);

    R updateCollection(CollectionEntity collectionEntity);

    R deleteCollection(Long collectionId);
}
