package com.javaweb.MichaelKai.quartz.service.impl;

import com.javaweb.MichaelKai.common.utils.SpringContextUtil;
import com.javaweb.MichaelKai.quartz.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-27 13:47
 **/
@Service
@Slf4j
public class QuartzServiceImpl implements QuartzService {

    private static final String METHOD_NAME = "execute";

    @Override
    public void executeTask(String beanName, String methodName) {
        Object bean = SpringContextUtil.getBean(beanName);
        try {
            log.info("[QuartzServiceImpl] 反射调beanName:{},methodName:{}法开始.........", beanName, methodName);
            if (beanName.contains("\\.")) {
                Class clazz = Class.forName(beanName);
                Object cronJob = SpringContextUtil.getBean(clazz);
                Method method = clazz.getMethod(methodName);
                method.invoke(cronJob);
            } else {
                Method method = bean.getClass().getMethod(methodName);
                method.invoke(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[QuartzServiceImpl] method invoke error,beanName:{},methodName:{}", beanName, methodName);
            return;
        }
        log.info("[QuartzServiceImpl] 反射调beanName:{},methodName:{}法结束.........", beanName, methodName);

    }

    @Override
    public void executeTask(String beanName) {
        executeTask(beanName, METHOD_NAME);
    }
}