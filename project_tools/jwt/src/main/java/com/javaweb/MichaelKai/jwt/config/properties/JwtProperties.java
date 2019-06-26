package com.javaweb.MichaelKai.jwt.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-24 11:36
 **/
@Data
@Component
@ConfigurationProperties(prefix = "project.jwt")
public class JwtProperties {
    //jwt密钥
    private String secret = "mySecret";
    //过期时间(秒), 默认2h
    private int expired = 7200;
    //权限模式-路径拦截
    private boolean patternPath = true;
    //权限模式-注解拦截
    private boolean patternAnno = false;
}