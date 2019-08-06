package com.javaweb.MichaelKai.activiti.service.customize.factory;

import com.javaweb.MichaelKai.activiti.service.customize.entity.CustomUserEntityManager;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: project_base
 * @description: 自定义的User的管理工厂类
 * @author: YuKai Fan
 * @create: 2019-08-06 16:03
 **/
@Service
public class CustomUserEntityManagerFactory implements SessionFactory {

    @Autowired
    private CustomUserEntityManager customUserEntityManager;

    @Override
    public Class<?> getSessionType() {
        //注意此处必须为Activiti原生类
        return UserIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return customUserEntityManager;
    }

    public void setCustomUserEntityManager(CustomUserEntityManager customUserEntityManager) {
        this.customUserEntityManager = customUserEntityManager;
    }
}