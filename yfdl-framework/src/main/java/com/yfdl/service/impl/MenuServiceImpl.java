package com.yfdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import  com.yfdl.service.MenuService;
import com.yfdl.mapper.MenuMapper;
import com.yfdl.entity.MenuEntity;
import org.springframework.stereotype.Service;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-09-29 15:31:08
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements MenuService {
}
