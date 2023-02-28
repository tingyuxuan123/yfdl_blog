package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.ImEntity;

/**
 * (Im)表服务接口
 *
 * @author makejava
 * @since 2023-02-22 17:50:54
 */
public interface ImService extends IService<ImEntity> {
    R chatUserList();

    R chatHistory(Long toId);
}
