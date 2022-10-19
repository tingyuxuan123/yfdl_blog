package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.dto.RoleDto;
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

    R roleList(Long currentPage,Long pageSize,String roleName,String status);

    R insertRole(RoleDto roleDto);

    R updateRole(RoleDto roleDto);

    R deleteRole(Long roleId);
}
