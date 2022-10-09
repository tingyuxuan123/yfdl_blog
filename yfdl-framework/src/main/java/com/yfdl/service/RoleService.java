package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.entity.RoleEntity;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-10-02 15:45:30
 */
public interface RoleService extends IService<RoleEntity> {
    List<String> selectRoleKeyByUserId(Long userId);
}
