package com.javaweb.MichaelKai.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: project_base
 * @description: 系统监控properties
 * @author: YuKai Fan
 * @create: 2019-06-04 09:36
 **/
@Component
@ConfigurationProperties(prefix = "project.monitor")
@Data
public class MonitorProperties {
    private int CPU_TIME = 5000;

    private int PERCENT = 100;

    private int FAULT_LENGTH = 10;
}