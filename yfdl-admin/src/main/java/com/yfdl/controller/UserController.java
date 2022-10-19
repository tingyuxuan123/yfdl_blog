package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.dto.LoginDto;
import com.yfdl.entity.UserEntity;
import com.yfdl.service.AdminLoginService;
import com.yfdl.service.UserService;
import com.yfdl.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminLoginService adminLoginService;

    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    @PostMapping("/user/login")
    public R login(@RequestBody LoginDto loginDto){
        UserEntity userEntity = BeanCopyUtils.copyBean(loginDto, UserEntity.class);
        return adminLoginService.login(userEntity);
    }

    /**
     * 注册
     * @return
     */
    @PostMapping("/user/register")
    public R register(@RequestBody UserEntity user){
        return userService.register(user);
    }



    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/getInfo")
    public R getInfo(){
        return userService.getInfo();
    }


    @GetMapping("/getRouters")
    public R getRouters(){
        return userService.getRouters();
    }

    /**
     * 更新用户信息
     * 需要登录
     */
    @PostMapping("user/updateInfo")
    public R updateInfo(@RequestBody UserEntity user){
        return userService.updateInfo(user);
    }

    /**
     * 重设密码需要登录
     * @param user
     * @return
     */
    @PostMapping("user/updatePassword")
    public R updatePassword(@RequestBody UserEntity user){
        return userService.updatePassword(user);
    }


    @GetMapping("/user/userList")
    public R userList(@RequestParam Long currentPage,
                      @RequestParam Long pageSize,
                      @RequestParam(required = false) String userName,
                      @RequestParam(required = false) String phonenumber,
                      @RequestParam(required = false) String status ){

        return  userService.userList(currentPage,pageSize,userName,phonenumber,status);
    }

}
