package com.yfdl.handler.exception;

import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.common.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //拦截自定义的异常
    @ExceptionHandler(SystemException.class)
    public R systemExceptionHandler(SystemException e){
        //打印异常学校
        log.error("发生了异常:{}",e);

        //从异常对象中获取提示信息封装返回
        return R.errorResult(e.getCode(), "运行出错");
    }


    @ExceptionHandler(Exception.class)
    public R exceptionHandler(RuntimeException e){
        //打印异常学校
        log.error("发生了异常:{}",e);

        //从异常对象中获取提示信息封装返回
        return R.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,e.getMessage());
    }

}
