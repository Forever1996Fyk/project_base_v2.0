package com.javaweb.MichaelKai.service;

import com.javaweb.MichaelKai.entity.MonitorInfoEntity;

/**
 * @program: project_base
 * @description: 系统监控接口
 * @author: YuKai Fan
 * @create: 2019-06-04 09:33
 **/
public interface MonitorService {

    /**
     * 获取当前的监控对象
     * @return MonitorInfoEntity
     * @throws Exception
     */
    MonitorInfoEntity getMonitorInfo() throws Exception;
}