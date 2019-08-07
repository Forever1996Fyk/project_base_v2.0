package com.javaweb.MichaelKai.aop;

import com.javaweb.MichaelKai.common.exception.ExceptionHandle;
import com.javaweb.MichaelKai.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: project_base
 * @description: 接口异常aop切面类
 * @author: YuKai Fan
 * @create: 2019-05-27 13:39
 **/
@Aspect
@Component
@Slf4j
public class HttpAspectAop {

    @Autowired
    private ExceptionHandle exceptionHandle;

    /**
     * 切点
     */
    @Pointcut("execution(public * com.javaweb.MichaelKai.*.*(..))")
    public void log() {

    }

    /**
     * 前置切面
     * @param joinPoint
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //url
        log.info("url={}", request.getRequestURL());
        //method
        log.info("method={}", request.getMethod());
        //ip
        log.info("ip={}", request.getRemoteAddr());
        //class_method
        log.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "," + joinPoint.getSignature());
        //args[]参数
        log.info("arg={}", joinPoint.getArgs());
    }

    /**
     * 环绕切面
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("log()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Result result = null;
        try {

        } catch (Exception e) {
            return exceptionHandle.exception(e);
        }

        if (result == null) {
            return proceedingJoinPoint.proceed();
        } else {
            return result;
        }
    }
}