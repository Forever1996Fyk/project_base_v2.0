package com.javaweb.MichaelKai.activiti.pojo;

import lombok.Data;

/**
 * @ClassName processDefinition
 * @Description 流程定义实体
 * @Author YuKai Fan
 * @Date 2019/8/7 21:26
 * @Version 1.0
 **/
@Data
public class ProcessDefinition {
    private String id;
    private String category;
    private String name;
    private String key;
    private String description;
    private int version;
    private String resourceName;
    private String deploymentId;
    private String diagramResourceName;
    private boolean hasStartFormKey;
    private boolean hasGraphicalNotation;
    private boolean isSuspended;
    private String tenantId;

    public ProcessDefinition() {
    }
    public ProcessDefinition(org.activiti.engine.repository.ProcessDefinition p) {
        this.id=p.getId();
        this.category=p.getCategory();
        this.name=p.getName();
        this.key=p.getKey();
        this.description=p.getDescription();
        this.version=p.getVersion();
        this.resourceName=p.getResourceName();
        this.deploymentId=p.getDeploymentId();
        this.diagramResourceName=p.getDiagramResourceName();
        this.hasStartFormKey=p.hasStartFormKey();
        this.hasGraphicalNotation=p.hasGraphicalNotation();
        this.isSuspended=p.isSuspended();
        this.tenantId=p.getTenantId();

    }
}
