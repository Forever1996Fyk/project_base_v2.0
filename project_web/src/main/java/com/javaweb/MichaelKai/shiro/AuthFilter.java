package com.javaweb.MichaelKai.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: project_base
 * @description: session超时拦截器
 * @author: YuKai Fan
 * @create: 2019-05-27 16:10
 **/
public class AuthFilter extends AccessControlFilter {

    /**
     * 表示是否允许访问;Object o就是[urls]配置中拦截器参数部分。如果允许访问返回true,否则false
     * 如果isAccessAllowed返回true则onAccessDenied方法不会继续执行
     * @param servletRequest
     * @param servletResponse
     * @param o 表示写在拦截器中括号里面的字符串 Object o 就是 [urls] 配置中拦截器参数部分
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        if (isLoginRequest(servletRequest, servletResponse)) {
                return true;
        } else {
            Subject subject = getSubject(servletRequest, servletResponse);
            return subject.getPrincipal() != null;
        }
    }

    /**
     * 表示当访问拒绝时，是否已经处理了;如果返回true表示需要继续出路;如果返回false表示该拦截器实例已经处理了,直接返回即可。
     * 从源码中可以看出onAccessDenied是否执行取决于isAccessAllowed是否返回false,如果返回true则onAccessDenied不会执行
     * 如果onAccessDenied也返回false，则直接返回，不会进入请求的方法（只有isAccessAllowed和onAccessDenied的情况下）
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(servletResponse);
        if (httpServletRequest.getHeader("X-Requested-With") != null
                && httpServletRequest.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value());
        } else {
            //跳转到ShiroFilterFactoryBean中配置的登录页面
            redirectToLogin(httpServletRequest, httpServletResponse);
        }
        return false;
    }
}