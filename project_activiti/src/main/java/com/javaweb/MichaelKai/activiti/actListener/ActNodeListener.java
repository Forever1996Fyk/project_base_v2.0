package com.javaweb.MichaelKai.activiti.actListener;

import com.javaweb.MichaelKai.activiti.service.ActAssigneeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: project_base
 * @description: 流程监听器 动态注入节点办理人
 * @author: YuKai Fan
 * @create: 2019-08-07 11:52
 **/
public class ActNodeListener implements TaskListener {
    @Autowired
    private ActAssigneeService actAssigneeService;

    @Override
    public void notify(DelegateTask delegateTask) {
        String nodeId = delegateTask.getTaskDefinitionKey();
    }
}