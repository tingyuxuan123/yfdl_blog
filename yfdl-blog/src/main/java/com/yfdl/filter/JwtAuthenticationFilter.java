package com.yfdl.filter;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.util.StringUtils;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.service.impl.LoginUser;
import com.yfdl.utils.JwtUtil;
import com.yfdl.utils.RedisCache;
import com.yfdl.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token=httpServletRequest.getHeader("token");
        if(Objects.isNull(token)){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        Claims claims;
        try {
             claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token 超时
            //token 非法
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(R.errorResult(AppHttpCodeEnum.NEED_LOGIN)));
            return;
        }
        String userId = claims.getSubject();
        //解析获取userid
        LoginUser loginUser = redisCache.getCacheObject("bloglogin" + userId);
        //如果获取不到
        if(Objects.isNull(loginUser)){
            //登录过期
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(R.errorResult(AppHttpCodeEnum.NEED_LOGIN)));
            return;
        }


        // 存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
