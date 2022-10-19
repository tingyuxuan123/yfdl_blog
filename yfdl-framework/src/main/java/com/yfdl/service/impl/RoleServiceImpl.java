package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.dto.RoleDto;
import com.yfdl.entity.RoleMenuEntity;
import com.yfdl.service.RoleMenuService;
import com.yfdl.service.RoleService;
import com.yfdl.mapper.RoleMapper;
import com.yfdl.entity.RoleEntity;
import com.yfdl.service.UserRoleService;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.vo.PageVo;
import com.yfdl.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-10-02 15:45:30
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long userId) {
        //查询用户id 对应的role_key 多表联查

        return getBaseMapper().selectRoleKeyByUserId(userId);

    }

    @Override
    public R roleList(Long currentPage,Long pageSize,String roleName,String status) {

        Page<RoleEntity> roleEntityPage = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<RoleEntity> roleEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleEntityLambdaQueryWrapper.eq(Objects.nonNull(roleName),RoleEntity::getRoleName,roleName);
        roleEntityLambdaQueryWrapper.eq(Objects.nonNull(status),RoleEntity::getStatus,status);
        roleEntityLambdaQueryWrapper.orderByAsc(RoleEntity::getRoleSort);
        page(roleEntityPage,roleEntityLambdaQueryWrapper);

        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(roleEntityPage.getRecords(), RoleVo.class);

        roleVos.stream().forEach(roleVo -> { //遍历角色列表，获取对应的角色id
            LambdaQueryWrapper<RoleMenuEntity> roleMenuEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleMenuEntityLambdaQueryWrapper.eq(RoleMenuEntity::getRoleId,roleVo.getId());
            List<RoleMenuEntity> list = roleMenuService.list(roleMenuEntityLambdaQueryWrapper); //根据觉得id获取对应的菜单
            //获取拥有菜单的全部id
            List<Long> collect = list.stream().map(RoleMenuEntity::getMenuId).collect(Collectors.toList());
            roleVo.setRoleMenu(collect);

        });

        PageVo<RoleVo> roleEntityPageVo = new PageVo<>(roleVos,roleEntityPage.getTotal());

        return R.successResult(roleEntityPageVo);
    }

    @Override
    @Transactional
    public R insertRole(RoleDto roleDto) {
        RoleEntity role = BeanCopyUtils.copyBean(roleDto, RoleEntity.class);
        save(role);
        Long id = role.getId();
        if(Objects.nonNull(roleDto.getRoleMenu())){
            addRoleMenu(id,roleDto);
        }

        return R.successResult();
    }

    /**
     * 给权限对应的菜单
     * @param id
     * @param roleDto
     */
    private void addRoleMenu(Long id,RoleDto roleDto){
        List<RoleMenuEntity> collect = roleDto.getRoleMenu().stream().map(menuId -> {
            RoleMenuEntity roleMenuEntity = new RoleMenuEntity();
            roleMenuEntity.setRoleId(id);
            roleMenuEntity.setMenuId(menuId);
            return roleMenuEntity;
        }).collect(Collectors.toList());

        roleMenuService.saveBatch(collect);
    }

    @Override
    @Transactional
    public R updateRole(RoleDto roleDto) {
        RoleEntity roleEntity = BeanCopyUtils.copyBean(roleDto, RoleEntity.class);
        updateById(roleEntity);

        if(Objects.nonNull(roleDto.getRoleMenu())){
            addRoleMenu(roleDto.getId(),roleDto);
        }


        return R.successResult();
    }

    /**
     * 删除角色对应的菜单
     * @param roleId
     */
    private void delete_role(Long roleId){
        //移除之前的权限
        LambdaQueryWrapper<RoleMenuEntity> roleMenuEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuEntityLambdaQueryWrapper.eq(RoleMenuEntity::getRoleId,roleId);
        roleMenuService.remove(roleMenuEntityLambdaQueryWrapper);
    }

    @Override
    public R deleteRole(Long roleId) {
        removeById(roleId);
        delete_role(roleId);
        return null;
    }


}
