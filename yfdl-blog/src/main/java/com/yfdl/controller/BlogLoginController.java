package com.yfdl.controller;


import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.common.SystemException;
import com.yfdl.entity.UserEntity;
import com.yfdl.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public R login(@RequestBody UserEntity user){
        if(!StringUtils.hasText(user.getUserName())){
                throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        } else if (!StringUtils.hasText(user.getPassword())) {
                throw new SystemException(AppHttpCodeEnum.REQUIRE_PASSWORD);
        }

        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public R logout(){
        return blogLoginService.logout();
    }

}
