package com.yfdl.handler.security;

import com.alibaba.fastjson.JSON;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 有权失败的异常拦截
 */
@Component
public class
AccessDenieHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        //打印异常
        e.printStackTrace();

        R r = R.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        //响应给前端
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(r));
    }
}
