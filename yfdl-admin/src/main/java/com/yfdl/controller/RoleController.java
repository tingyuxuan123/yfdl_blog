package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.dto.RoleDto;
import com.yfdl.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("roleList")
    public R roleList(@RequestParam Long currentPage,
                      @RequestParam Long pageSize,
                      @RequestParam(required = false) String roleName,
                      @RequestParam(required = false) String status
                      ){
        return  roleService.roleList(currentPage,pageSize,roleName,status);
    }


    @PostMapping("insertRole")
    public R insertRole(@RequestBody RoleDto roleDto){
        return roleService.insertRole(roleDto);
    }

    @PostMapping("updateRole")
    public R updateRole(@RequestBody RoleDto roleDto){
        return roleService.updateRole(roleDto);
    }

    @GetMapping("deleteRole")
    public R deleteRole(@RequestParam Long roleId){
        return roleService.deleteRole(roleId);
    }

}
