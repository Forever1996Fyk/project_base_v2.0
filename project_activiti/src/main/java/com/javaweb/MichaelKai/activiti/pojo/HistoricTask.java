package com.javaweb.MichaelKai.activiti.pojo;

import lombok.Data;
import org.activiti.engine.history.HistoricTaskInstance;

import java.util.Date;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-08-08 14:50
 **/
@Data
public class HistoricTask {
    private String id;
    private String name;
    private Date startTime;
    private Date endTime;
    private Date createTime;
    private String assignee;
    private String assigneeName;
    private String processInstanceId;//流程实例id
    private String processDefinitionId;//流程定义id

    public HistoricTask() {
    }
    public HistoricTask(HistoricTaskInstance t) {
        this.id=t.getId();
        this.name=t.getName();
        this.createTime=t.getCreateTime();
        this.assignee=t.getAssignee();
        this.processInstanceId=t.getProcessInstanceId();
        this.processDefinitionId=t.getProcessDefinitionId();
        this.startTime = t.getStartTime();
        this.endTime = t.getEndTime();
    }
}