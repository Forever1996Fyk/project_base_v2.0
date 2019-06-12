package com.javaweb.MichaelKai.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-12 08:53
 **/
@Component
@ConfigurationProperties(prefix = "netty.server")
@Data
public class ServerProperties {
    //netty服务端口
    private int port = 9090;
}