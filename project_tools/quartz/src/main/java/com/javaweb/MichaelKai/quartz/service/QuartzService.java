package com.javaweb.MichaelKai.quartz.service;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-27 13:45
 **/
public interface QuartzService {

    /**
     * 根据bean名称，方法名 执行任务
     * @param beanName bean名称
     * @param methodName 方法名称
     */
    void executeTask(String beanName, String methodName);

    /**
     * 根据bean名称执行任务
     * @param beanName bean名称
     */
    void executeTask(String beanName);
}