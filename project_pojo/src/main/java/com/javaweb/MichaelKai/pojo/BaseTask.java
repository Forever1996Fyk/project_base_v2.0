package com.javaweb.MichaelKai.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: project_base
 * @description: 基础任务结构实体
 * @author: YuKai Fan
 * @create: 2019-08-08 14:43
 **/
@Data
public abstract class BaseTask implements Serializable {

    protected String id;
    protected String userId;
    protected String processInstanceId;
    protected Integer status;
    protected String reason;
    protected String taskName;//实时节点信息
    protected String urlPath;
    private String  createTime;
    private String createUserId;
    private String updateTime;
    private String updateUserId;

}