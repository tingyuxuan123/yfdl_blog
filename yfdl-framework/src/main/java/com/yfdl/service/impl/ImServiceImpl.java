package com.yfdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import  com.yfdl.service.ImService;
import com.yfdl.mapper.ImMapper;
import com.yfdl.entity.ImEntity;
import com.yfdl.utils.SecurityUtils;
import com.yfdl.vo.im.ChatUserListVo;
import org.springframework.stereotype.Service;

/**
 * (Im)表服务实现类
 *
 * @author makejava
 * @since 2023-02-22 17:50:54
 */
@Service
public class ImServiceImpl extends ServiceImpl<ImMapper, ImEntity> implements ImService {
    @Override
    public R chatUserList() {
        //获取登录用于id
        Long userId = SecurityUtils.getUserId();

       ChatUserListVo[] chatUserListVo= baseMapper.chatUserList(userId);

        return R.successResult(chatUserListVo);
    }

    @Override
    public R chatHistory(Long toId) {
        //获取登录用于id
        Long userId = SecurityUtils.getUserId();

        ImEntity[] imEntities =baseMapper.chatHistory(userId,toId);

        return R.successResult(imEntities);
    }
}
