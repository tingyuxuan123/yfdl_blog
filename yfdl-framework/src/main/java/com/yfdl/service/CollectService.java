package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.dto.collect.InsertCollectDto;
import com.yfdl.entity.CollectEntity;

import java.util.List;

/**
 * (Collect)表服务接口
 *
 * @author makejava
 * @since 2022-11-13 20:07:31
 */
public interface CollectService extends IService<CollectEntity> {


    R insertCollect(InsertCollectDto insertCollectDto);

    R cancelCollect(Long articleId);
}
