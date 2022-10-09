package com.yfdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.service.RoleMenuService;
import com.yfdl.mapper.RoleMenuMapper;
import com.yfdl.entity.RoleMenuEntity;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-10-02 10:59:46
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenuEntity> implements RoleMenuService {
}
