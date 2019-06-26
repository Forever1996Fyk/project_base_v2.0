package com.javaweb.MichaelKai.jwt.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.javaweb.MichaelKai.common.enums.JwtResultEnums;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.utils.JwtUtil;
import com.javaweb.MichaelKai.jwt.annotation.IgnorePermissions;
import com.javaweb.MichaelKai.jwt.config.properties.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @program: project_base
 * @description: jwt认证拦截器
 * @author: YuKai Fan
 * @create: 2019-06-24 11:47
 **/
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //判断请求映射的方式是否忽略权限验证
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(IgnorePermissions.class)) {
            return true;
        }

        //获取请求头token数据
        String token = JwtUtil.getRequestToken(request, "access_token");
        //String account = JwtUtil.getRequestToken(request, "account");

        //验证token数据是否正确
        try {
            JwtUtil.verifyToken(token, jwtProperties.getSecret());
        } catch (TokenExpiredException e) {
            throw new ResultException(JwtResultEnums.TOKEN_EXPIRED.getCode(), JwtResultEnums.TOKEN_EXPIRED.getMessage());
        } catch (JWTVerificationException e) {
            throw new ResultException(JwtResultEnums.TOKEN_ERROR.getCode(), JwtResultEnums.TOKEN_ERROR.getMessage());
        }

        return true;
    }
}