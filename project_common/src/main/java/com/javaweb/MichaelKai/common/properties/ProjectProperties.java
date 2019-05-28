package com.javaweb.MichaelKai.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: project_base
 * @description: 项目配置
 * @author: YuKai Fan
 * @create: 2019-05-27 16:36
 **/
@Component
@ConfigurationProperties(prefix = "project")
@Data
public class ProjectProperties {
    //是否开启验证码
    private boolean captchOpen = true;
}