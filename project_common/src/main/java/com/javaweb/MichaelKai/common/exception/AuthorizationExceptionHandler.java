package com.javaweb.MichaelKai.common.exception;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.utils.SpringContextUtil;
import com.javaweb.MichaelKai.common.vo.Result;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: project_base
 * @description: 拦截访问权限异常
 * @author: YuKai Fan
 * @create: 2019-05-29 16:22
 **/
@ControllerAdvice
@Order(0)//Order(value)表示优先级，value越小,优先级越高
public class AuthorizationExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public Result authorizationException(AuthorizationException e, HttpServletRequest request,
                                         HttpServletResponse response){
        Throwable cause = e.getCause();
        String message = cause.getMessage();
        Class<Result> resultClass = Result.class;

        if (!message.contains(resultClass.getName())) {
            try {
                //重定向到无权限页面
                String contextPath = request.getContextPath();
                ShiroFilterFactoryBean shiroFilter = SpringContextUtil.getBean(ShiroFilterFactoryBean.class);
                response.sendRedirect(contextPath + shiroFilter.getUnauthorizedUrl());
            } catch (IOException e1) {
                return new Result(false, ResultEnum.NO_PERMISSIONS.getValue(), ResultEnum.NO_PERMISSIONS.getMessage());
            }
        }

        return new Result(false, ResultEnum.NO_PERMISSIONS.getValue(), ResultEnum.NO_PERMISSIONS.getMessage());

    }
}