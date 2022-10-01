package com.yfdl.aspect;


import com.alibaba.fastjson.JSON;
import com.yfdl.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@Aspect
public class LogAspect {

    //确定切点
    @Pointcut("@annotation(com.yfdl.annotation.SystemLog)")
    public void pt(){

    }


    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint)throws Throwable{



        Object ret;
         try {
             handleBefore(joinPoint);
             ret= joinPoint.proceed();
             handleAfter(ret);
         }finally {
             log.info("=======End=======" + System.lineSeparator()); //系统的换行符
         }

        return ret;

    }

    private void handleAfter(Object ret) {

        // 打印出参
        log.info("Response       : {}",JSON.toJSONString(ret) );
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        SystemLog systemLog=getSystemLog(joinPoint);


        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURI());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));

    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        return signature.getMethod().getAnnotation(SystemLog.class);
    }

}
