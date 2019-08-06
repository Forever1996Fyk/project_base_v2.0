package com.javaweb.MichaelKai.activiti.service.customize.factory;

import com.javaweb.MichaelKai.activiti.service.customize.entity.CustomGroupEntityManager;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: project_base
 * @description: 自定义Group管理工厂
 * @author: YuKai Fan
 * @create: 2019-08-06 16:06
 **/
@Service
public class CustomGroupEntityManagerFactory implements SessionFactory {
    @Autowired
    private CustomGroupEntityManager customGroupEntityManager;

    @Override
    public Class<?> getSessionType() {
        // 返回原始的GroupManager类型
        return GroupEntityManager.class;
    }

    @Override
    public Session openSession() {
        return customGroupEntityManager;
    }

    public void setCustomGroupEntityManager(CustomGroupEntityManager customGroupEntityManager) {
        this.customGroupEntityManager = customGroupEntityManager;
    }
}