package com.javaweb.MichaelKai.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: project_base
 * @description: shiro配置类
 * @author: YuKai Fan
 * @create: 2019-05-27 15:58
 **/
@Component
@ConfigurationProperties(prefix = "project.shiro")
@Data
public class ShiroProperties {
    //cookie记住登录信息时间,默认7天
    private Integer rememberMeTimeout = 7;
    //session回话超时时间,默认30分钟
    private Integer globalSessionTimeout = 1800;
    //session会话检测间隔时间,默认15分钟
    private Integer sessionValidationInterval = 900;
}