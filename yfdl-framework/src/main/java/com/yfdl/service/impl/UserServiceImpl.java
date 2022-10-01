package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.common.SystemException;
import com.yfdl.service.UserService;
import com.yfdl.mapper.UserMapper;
import com.yfdl.entity.UserEntity;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.utils.SecurityUtils;
import com.yfdl.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-09-25 19:28:22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public R userInfo() {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        //获取用户信息
        UserEntity user = getById(userId);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return R.successResult(userInfoVo);
    }

    @Override
    public R updateUserInfo(UserEntity user) {

        boolean b = updateById(user);


        return R.successResult();


    }

    @Override
    public R register(UserEntity user) {
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }

        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }

        //对密码进行加密

        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);

        save(user);
        return R.successResult();
    }

    public boolean userNameExist(String username){
        LambdaQueryWrapper<UserEntity> userEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userEntityLambdaQueryWrapper.eq(UserEntity::getUserName,username);
        List<UserEntity> list = list(userEntityLambdaQueryWrapper);
        if (list.size()>=1){
            return true;
        }else {
            return false;
        }

    }

    public boolean nickNameExist(String nickname){
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getNickName,nickname);
        List<UserEntity> list = list(queryWrapper);
        if (list.size()>=1){
            return true;
        }else {
            return false;
        }
    }


}
