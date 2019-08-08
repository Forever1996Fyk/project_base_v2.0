package com.javaweb.MichaelKai.activiti.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-08-08 14:50
 **/
@Data
public class Task {
    private String id;
    private String name;
    private Date createTime;
    private String assignee;
    private String processInstanceId;//流程实例id
    private String processDefinitionId;//流程定义id
    private String description;
    private String category;

    private String userName;
    private String reason;
    private String urlPath;

    private Boolean flag;
    public Task() {
    }
    public Task(org.activiti.engine.task.Task t) {
        this.id=t.getId();
        this.name=t.getName();
        this.createTime=t.getCreateTime();
        this.assignee=t.getAssignee();
        this.processInstanceId=t.getProcessInstanceId();
        this.processDefinitionId=t.getProcessDefinitionId();
        this.description=t.getDescription();
        this.category=t.getCategory();
    }
}