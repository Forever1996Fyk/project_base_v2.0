package com.javaweb.MichaelKai.jwt.config;

import com.javaweb.MichaelKai.jwt.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: project_base
 * @description: jwt拦截器配置
 * @author: YuKai Fan
 * @create: 2019-06-24 11:35
 **/
@Configuration
@ConditionalOnProperty(name = "project.jwt.pattern-path", havingValue = "true", matchIfMissing = true)
public class JwtInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/app/api/**").excludePathPatterns("/app/api/auth");
    }
}