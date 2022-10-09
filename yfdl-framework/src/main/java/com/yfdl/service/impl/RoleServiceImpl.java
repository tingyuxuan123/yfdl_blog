package com.yfdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.service.RoleService;
import com.yfdl.mapper.RoleMapper;
import com.yfdl.entity.RoleEntity;
import com.yfdl.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-10-02 15:45:30
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {


    @Override
    public List<String> selectRoleKeyByUserId(Long userId) {
        //查询用户id 对应的role_key 多表联查

        return getBaseMapper().selectRoleKeyByUserId(userId);

    }
}
