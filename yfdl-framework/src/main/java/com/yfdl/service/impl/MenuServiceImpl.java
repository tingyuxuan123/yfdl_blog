package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.constants.SystemConstants;
import com.yfdl.service.MenuService;
import com.yfdl.mapper.MenuMapper;
import com.yfdl.entity.MenuEntity;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.vo.MenuVo;
import org.springframework.stereotype.Service;

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
      if(userId==1L){  //如果id为1找到所有复合条件的
          LambdaQueryWrapper<MenuEntity> menuEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
          menuEntityLambdaQueryWrapper.eq(MenuEntity::getStatus,'0');
          menuEntityLambdaQueryWrapper.in(MenuEntity::getMenuType,SystemConstants.Category,SystemConstants.MENU);
          menuEntityLambdaQueryWrapper.eq(MenuEntity::getParentId,0);
          menuEntityLambdaQueryWrapper.orderByAsc(MenuEntity::getParentId,MenuEntity::getOrderNum);
          List<MenuEntity> list = list(menuEntityLambdaQueryWrapper);
          List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);

          menuVos.forEach(this::getChildren);



          return menuVos;

      }

      List<MenuEntity> menuEntities= getBaseMapper().selectRootMenuByUserId(userId);
      List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menuEntities, MenuVo.class);

      menuVos.forEach(this::getChildren);



      return menuVos;
    }

    public void getChildren(MenuVo menuVo){

            LambdaQueryWrapper<MenuEntity> menuEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            menuEntityLambdaQueryWrapper.eq(MenuEntity::getParentId, menuVo.getId());
            menuEntityLambdaQueryWrapper.eq(MenuEntity::getStatus,'0');
            menuEntityLambdaQueryWrapper.in(MenuEntity::getMenuType,SystemConstants.Category,SystemConstants.MENU);
            List<MenuEntity> list = list(menuEntityLambdaQueryWrapper);
            List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);
            menuVo.setChildren(menuVos);
        }

}
