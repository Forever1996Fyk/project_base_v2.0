package com.javaweb.MichaelKai.jwt.annotation;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.enums.JwtResultEnums;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.utils.JwtUtil;
import com.javaweb.MichaelKai.common.utils.RedisUtil;
import com.javaweb.MichaelKai.jwt.config.properties.JwtProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-24 11:33
 **/
@Aspect
@Component
@ConditionalOnProperty(name = "project.jwt.pattern-anno", havingValue = "true")
public class JwtPermissionsAop {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(com.javaweb.MichaelKai.jwt.annotation.JwtPermissions)")
    public void jwtPermissions() {}

    @Around("jwtPermissions()")
    public Object doPermission(ProceedingJoinPoint point) throws Throwable {

        //获取请求头数据
        String token = JwtUtil.getRequestToken(request, "access_token");
        String account = JwtUtil.getRequestToken(request, "account");

        //判断token是否在黑名单中
        if (redisUtil.hasKey(Constant.JWT_TOKEN_BLACKLIST + token)) {
            throw new ResultException(JwtResultEnums.TOKEN_ERROR.getCode(), JwtResultEnums.TOKEN_ERROR.getMessage());
        }

        //验证token是否正确
        try {
            JwtUtil.verifyToken(token, jwtProperties.getSecret());
        } catch (TokenExpiredException e) {
            //判断token在redis中是否过期,如果redis中token未过期则重新刷新token,返回前端
            if (redisUtil.hasKey(Constant.JWT_TOKEN + account)) {
                String tokenNew = JwtUtil.getToken(account, jwtProperties.getSecret(), jwtProperties.getExpired());
                redisUtil.set(Constant.JWT_TOKEN + account, tokenNew, jwtProperties.getExpired() * 2);
                throw new ResultException(JwtResultEnums.TOKEN_REFRESH.getCode(), JwtResultEnums.TOKEN_REFRESH.getMessage(), tokenNew);

            } else {//如果redis中token过期，则需要重新登录
                throw new ResultException(JwtResultEnums.TOKEN_EXPIRED.getCode(), JwtResultEnums.TOKEN_EXPIRED.getMessage());
            }
        } catch (JWTVerificationException e) {//token错误
            throw new ResultException(JwtResultEnums.TOKEN_ERROR.getCode(), JwtResultEnums.TOKEN_ERROR.getMessage());
        }

        return point.proceed();
    }
}