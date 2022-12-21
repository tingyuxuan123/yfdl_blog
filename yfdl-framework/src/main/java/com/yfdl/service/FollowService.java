package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.FollowEntity;

/**
 * (Follow)表服务接口
 *
 * @author makejava
 * @since 2022-11-08 22:26:51
 */
public interface FollowService extends IService<FollowEntity> {
    R follow(Long followId);

    R followList(Long id);
}
