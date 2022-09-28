package com.yfdl.handler.security;

import com.alibaba.fastjson.JSON;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.utils.WebUtils;
import org.apache.http.HttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //打印异常
       e.printStackTrace();
        R r =R.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或授权失败");;

       if(e instanceof BadCredentialsException){
          r = R.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),e.getMessage());
       } else if (e instanceof InsufficientAuthenticationException) {
           r = R.errorResult(AppHttpCodeEnum.NEED_LOGIN.getCode(),e.getMessage());
       }


        //响应给前端
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(r));
    }
}
