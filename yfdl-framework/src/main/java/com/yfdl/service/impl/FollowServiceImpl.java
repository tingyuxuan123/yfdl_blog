package com.yfdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import  com.yfdl.service.FollowService;
import com.yfdl.mapper.FollowMapper;
import com.yfdl.entity.FollowEntity;
import org.springframework.stereotype.Service;

/**
 * (Follow)表服务实现类
 *
 * @author makejava
 * @since 2022-11-08 22:26:51
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, FollowEntity> implements FollowService {
}
