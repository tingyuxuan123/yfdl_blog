package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.entity.UserEntity;
import com.yfdl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    public R userInfo(){
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    public R userInfo(@RequestBody UserEntity user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    public R register(@RequestBody UserEntity user){
        return userService.register(user);
    }

}
