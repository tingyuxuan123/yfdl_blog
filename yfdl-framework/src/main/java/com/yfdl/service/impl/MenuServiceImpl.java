package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.constants.SystemConstants;
import com.yfdl.entity.RoleMenuEntity;
import com.yfdl.service.MenuService;
import com.yfdl.mapper.MenuMapper;
import com.yfdl.entity.MenuEntity;
import com.yfdl.service.RoleMenuService;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-10-02 10:59:09
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectPermsByUserId(Long userId) {

        if(userId==1L){
            LambdaQueryWrapper<MenuEntity> menuEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            menuEntityLambdaQueryWrapper.in(MenuEntity::getMenuType, SystemConstants.Category,SystemConstants.BUTTON);
            menuEntityLambdaQueryWrapper.eq(MenuEntity::getStatus,SystemConstants.CATEGORY_STATUS_NORMAL);
            List<MenuEntity> menus = list(menuEntityLambdaQueryWrapper);
            List<String> perms = menus.stream()
                    .map(MenuEntity::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        return getBaseMapper().selectPermsByUserId(userId);
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        //获取根据id获取对应权限的根菜单根菜单
      if(userId==1L){  //如果id为1找到所有复合条件的 1为超级管理员
          LambdaQueryWrapper<MenuEntity> menuEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
//          menuEntityLambdaQueryWrapper.eq(MenuEntity::getStatus,'0');
          menuEntityLambdaQueryWrapper.in(MenuEntity::getMenuType,SystemConstants.Category,SystemConstants.MENU);
          menuEntityLambdaQueryWrapper.eq(MenuEntity::getParentId,0);
          menuEntityLambdaQueryWrapper.orderByAsc(MenuEntity::getParentId,MenuEntity::getOrderNum);
          List<MenuEntity> list = list(menuEntityLambdaQueryWrapper);
          List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);

          menuVos.forEach(menuVo -> getChildren(menuVo,userId));
          return menuVos;
      }

      List<MenuEntity> menuEntities= getBaseMapper().selectRootMenuByUserId(userId);
      List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menuEntities, MenuVo.class);

      menuVos.forEach(menuVo -> getChildren(menuVo,userId));



      return menuVos;
    }

    @Transactional
    @Override
    public R deleteMenu(Long id) {
        //删除菜单
        removeById(id);

        //删除对应菜单的权限
        LambdaQueryWrapper<RoleMenuEntity> roleMenuEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuEntityLambdaQueryWrapper.eq(RoleMenuEntity::getMenuId,id);
        roleMenuService.remove(roleMenuEntityLambdaQueryWrapper);

        return R.successResult();
    }

    public void getChildren(MenuVo menuVo,Long userId){



            if(userId==1L){ //如果用户id 为1 禁用的也显示
                //判断
                LambdaQueryWrapper<MenuEntity> menuEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
                menuEntityLambdaQueryWrapper.eq(MenuEntity::getParentId, menuVo.getId());
                menuEntityLambdaQueryWrapper.orderByAsc(MenuEntity::getOrderNum);
                menuEntityLambdaQueryWrapper.in(MenuEntity::getMenuType,SystemConstants.Category,SystemConstants.MENU);
                List<MenuEntity> list = list(menuEntityLambdaQueryWrapper);
                List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);
                menuVo.setChildren(menuVos);
            }else {
                List<MenuEntity> children = getBaseMapper().getChildren(userId, menuVo.getId());
                List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(children, MenuVo.class);
                menuVo.setChildren(menuVos);
            }




    }

}
