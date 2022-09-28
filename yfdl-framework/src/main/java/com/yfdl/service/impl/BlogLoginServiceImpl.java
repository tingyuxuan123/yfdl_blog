package com.yfdl.service.impl;

import com.yfdl.common.R;
import com.yfdl.entity.UserEntity;
import com.yfdl.service.BlogLoginService;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.utils.JwtUtil;
import com.yfdl.utils.RedisCache;
import com.yfdl.vo.BlogUserLoginVo;
import com.yfdl.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public R login(UserEntity user) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //判断是否认证通过

        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码为空");
        }
        //获取userid 生成token
         LoginUser loginUser= (LoginUser)authenticate.getPrincipal();
         String userId=loginUser.getUser().getId().toString();
         String jwt = JwtUtil.createJWT(userId);

        //把用户存入redis
        redisCache.setCacheObject("bloglogin"+userId,loginUser);

        //把token 和userinfo封装 返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);

        return R.successResult(blogUserLoginVo);
    }

    @Override
    public R logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        LoginUser loginuser = (LoginUser)authentication.getPrincipal();
        Long userId = loginuser.getUser().getId();
        redisCache.deleteObject("bloglogin"+userId);
        return R.successResult();

    }

}
