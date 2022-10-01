package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.UserEntity;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-09-25 19:28:22
 */
public interface UserService extends IService<UserEntity> {
    R userInfo();

    R updateUserInfo(UserEntity user);

    R register(UserEntity user);
}
