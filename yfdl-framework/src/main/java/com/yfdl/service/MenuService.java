package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.MenuEntity;
import com.yfdl.vo.MenuVo;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-10-02 10:59:09
 */
public interface MenuService extends IService<MenuEntity> {

    List<String> selectPermsByUserId(Long userId);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);

    R deleteMenu(Long id);
}
