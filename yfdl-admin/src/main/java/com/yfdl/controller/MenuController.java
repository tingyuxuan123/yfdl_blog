package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.entity.MenuEntity;
import com.yfdl.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    //添加菜单
    @PostMapping("insertMenu")
    public R insertMenu(@RequestBody MenuEntity menu){

        return R.successResult(menuService.save(menu));
    }

    //修改菜单
    @PostMapping("updateMenu")
    public R updateMenu(@RequestBody MenuEntity menu){
        return R.successResult(menuService.updateById(menu));
    }

    //删除菜单
    @GetMapping("deleteMenu")
    public R deleteMenu(@RequestParam Long id){
        return menuService.deleteMenu(id);
    }

}
