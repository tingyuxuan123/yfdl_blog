package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yfdl.constants.SystemConstants;
import com.yfdl.entity.UserEntity;
import com.yfdl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<UserEntity> userEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userEntityLambdaQueryWrapper.eq(UserEntity::getUserName,username);

        UserEntity user = userService.getOne(userEntityLambdaQueryWrapper);
        if (Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        } else if (SystemConstants.USER_STATUS_NOTNORMAL.equals(user.getStatus())) {
            throw new RuntimeException("该账号已被禁用");
        }

        //判断是否查询到用户，如果没查到抛出异常
//        TODO 查询权限信息封装




        return new LoginUser(user);
    }
}
